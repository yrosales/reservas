package com.example.reservas.dto;

public record ResponsableDTO(
    Long id,
    String nombre,
    String apellido,
    String email,
    String telefono,
    String cargo,
    Boolean activo
) {}
