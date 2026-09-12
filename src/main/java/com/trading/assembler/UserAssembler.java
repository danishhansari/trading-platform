package com.trading.assembler;

import com.trading.dto.UserDTO;
import com.trading.entity.User;
import com.trading.pojo.RegisterUserPojo;

public class UserAssembler {
    private static UserAssembler instance;
    private UserAssembler() {}

    public static UserAssembler getInstance() {
        if(instance == null) {
            synchronized (UserAssembler.class) {
                if(instance == null) {
                    instance = new UserAssembler();
                }
            }
        }
        return instance;
    }

    public User assembleDTO (RegisterUserPojo pojo) {
        User user = new User();
        user.setName(pojo.getName());
        user.setEmail(pojo.getEmail());
        user.setRole(pojo.getRole());
        return user;
    }

    public UserDTO assembleDetails(User user, String jwt) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                jwt
        );
    }
}