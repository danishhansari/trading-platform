package com.trading.pojo;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserPojo {

    @Email(message = "Valid email is required")
    private String email;

    private String password;
}