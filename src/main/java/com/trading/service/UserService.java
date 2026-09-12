package com.trading.service;

import com.trading.dto.UserDTO;
import com.trading.pojo.RegisterUserPojo;

public interface UserService {
    UserDTO signup(RegisterUserPojo pojo) throws Exception;
    UserDTO login(String userName, String password) throws Exception;
}