package com.exchange.service;

import com.exchange.dto.UserDTO;
import com.exchange.pojo.RegisterUserPojo;

public interface UserService {
    UserDTO signup(RegisterUserPojo pojo) throws Exception;
    UserDTO login(String userName, String password) throws Exception;
}