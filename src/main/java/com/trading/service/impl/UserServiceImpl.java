package com.trading.service.impl;

import com.trading.assembler.UserAssembler;
import com.trading.constants.UserRole;
import com.trading.dto.UserDTO;
import com.trading.entity.User;
import com.trading.exception.UserAlreadyExists;
import com.trading.exception.UserNotFoundException;
import com.trading.pojo.RegisterUserPojo;
import com.trading.repo.UserRepo;
import com.trading.service.UserService;
import com.trading.service.WalletService;
import com.trading.service.security.CustomUserDetailsService;
import com.trading.service.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final WalletService walletService;

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public UserDTO signup(RegisterUserPojo pojo) throws Exception {
        userRepository.findByEmail(pojo.getEmail()).ifPresent(e -> {
            throw new UserAlreadyExists("Email already exists");
        });
        String hashedPassword = passwordEncoder.encode(pojo.getPassword());
        User user = UserAssembler.getInstance().assembleDTO(pojo);
        user.setPassword(hashedPassword);
        user = userRepository.save(user);
        if(user.getRole() == UserRole.TRADER) {
            walletService.createWallet(user.getId());
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                ));
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