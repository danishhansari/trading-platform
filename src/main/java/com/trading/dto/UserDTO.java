package com.trading.dto;

import com.trading.enums.UserRole;

public record UserDTO (
    Long id,
    String name,
    String email,
    UserRole role,
    String jwt
) {}
