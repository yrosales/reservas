package com.example.reservas.dto;

import java.time.Instant;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String codigo,
    String error,
    String message,
    String path
) {}
