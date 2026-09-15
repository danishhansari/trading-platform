package com.trading.service.impl;

import com.trading.assembler.UserAssembler;
import com.trading.enums.UserRole;
import com.trading.dto.UserDTO;
import com.trading.entity.User;
import com.trading.event.UserCreatedEvent;
import com.trading.exception.UserException;
import com.trading.pojo.RegisterUserPojo;
import com.trading.producers.UserEventProducer;
import com.trading.repo.UserRepo;
import com.trading.service.UserService;
import com.trading.security.CustomUserDetailsService;
import com.trading.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserEventProducer applicationEventPublisher;
    private final UserAssembler userAssembler;

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public UserDTO signup(RegisterUserPojo pojo) {
        userRepository.findByEmail(pojo.getEmail()).ifPresent(e -> {
            throw new UserException("Email already exists");
        });
        String hashedPassword = passwordEncoder.encode(pojo.getPassword());
        User user = userAssembler.assembleDTO(pojo);
        user.setPassword(hashedPassword);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserException("Email already exists");
        }
        if(user.getRole() == UserRole.TRADER) {
            applicationEventPublisher.publish(new UserCreatedEvent(user.getId()));
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                ));
        String jwtToken = jwtService.generateToken(authentication, user.getId());
        return userAssembler.assembleDetails(user, jwtToken);
    }

    @Override
    public UserDTO login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));
        Authentication authentication = authentication(password, user);
        String jwtToken = jwtService.generateToken(authentication,user.getId());
        return userAssembler.assembleDetails(user, jwtToken);
    }

    public Authentication authentication(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) throw new UserException("Invalid credentials");
        UserDetails userDetails = customUserDetailsService.loadUserByEntity(user);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}