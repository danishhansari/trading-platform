package com.trading.dto;

public record UserDTO (
    Long id,
    String name,
    String email,
    String jwt
) {}