package com.exchange.pojo;

import com.exchange.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserPojo {
    private String name;

    private String email;

    private String password;

    private UserRole role;
}