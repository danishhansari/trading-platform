package com.trading.pojo;

import com.trading.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserPojo {
    @NotNull(message = "Name is required to inboard")
    private String name;

    @Email(message = "email is required to register")
    private String email;

    private String password;

    private UserRole role;
}