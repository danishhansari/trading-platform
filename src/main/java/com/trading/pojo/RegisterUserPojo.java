package com.trading.pojo;

import com.trading.constants.UserRole;
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