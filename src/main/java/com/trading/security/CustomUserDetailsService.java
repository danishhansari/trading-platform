package com.trading.security;

import com.trading.entity.User;
import com.trading.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    public UserDetails loadUserByEntity(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        ));
    }
}