package com.trading.controller;

import com.trading.dto.UserDTO;
import com.trading.pojo.LoginUserPojo;
import com.trading.pojo.RegisterUserPojo;
import com.trading.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginUserPojo userPojo) throws Exception {
        UserDTO dto = userService.login(userPojo.getEmail(), userPojo.getPassword());
        ResponseCookie cookie = ResponseCookie
                .from("token", dto.jwt())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(dto);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody RegisterUserPojo userPojo) throws Exception {
        UserDTO dto = userService.signup(userPojo);
        ResponseCookie cookie = ResponseCookie
                .from("token", dto.jwt())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(Map.of("authenticated", true));
    }

}