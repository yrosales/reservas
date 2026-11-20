package com.example.reservas.dto;

public record LoginResponseDTO(
    String token,
    String tipo,       // "Bearer"
    long   expiraEnMs,
    String username,
    String rol
) {}