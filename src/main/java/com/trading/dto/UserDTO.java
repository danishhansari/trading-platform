package com.trading.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trading.enums.UserRole;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO (
    Long id,
    String name,
    String email,
    UserRole role,
    String jwt
) {}
