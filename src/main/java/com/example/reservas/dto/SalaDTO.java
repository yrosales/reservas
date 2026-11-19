package com.example.reservas.dto;

public record SalaDTO(
    Long id,
    String nombre,
    String ubicacion,
    Integer capacidad,
    String descripcion,
    Boolean activa
) {}
