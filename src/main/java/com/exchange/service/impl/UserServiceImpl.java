package com.exchange.service.impl;

import com.exchange.assembler.UserAssembler;
import com.exchange.dto.UserDTO;
import com.exchange.entity.User;
import com.exchange.exception.UserAlreadyExists;
import com.exchange.exception.UserNotFoundException;
import com.exchange.pojo.RegisterUserPojo;
import com.exchange.repo.UserRepo;
import com.exchange.service.UserService;
import com.exchange.service.security.CustomUserDetailsService;
import com.exchange.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public UserDTO signup(RegisterUserPojo pojo) throws Exception {
        userRepository.findByEmail(pojo.getEmail()).ifPresent(e -> {
            throw new UserAlreadyExists("Email already exists");
        });
        String hashedPassword = passwordEncoder.encode(pojo.getPassword());
        User user = UserAssembler.getInstance().assembleDTO(pojo);
        user.setPassword(hashedPassword);
        user = userRepository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        String jwtToken = jwtService.generateToken(authentication, user.getId());
        UserDTO dto = UserAssembler.getInstance().assembleDetails(user, jwtToken);
        return dto;
    }

    @Override
    public UserDTO login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Authentication authentication = authentication(password, user);
        String jwtToken = jwtService.generateToken(authentication,user.getId());
        UserDTO dto = UserAssembler.getInstance().assembleDetails(user, jwtToken);
        return dto;
    }

    public Authentication authentication(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) throw new UserNotFoundException("Invalid credentials");
        UserDetails userDetails = customUserDetailsService.loadUserByEntity(user);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}