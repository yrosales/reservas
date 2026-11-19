package com.example.reservas.dto;

import com.example.reservas.entity.EstadoReserva;

import java.time.LocalDateTime;

public record ReservaResponseDTO(
    Long id,
    SalaDTO sala,
    ResponsableDTO responsable,
    String asunto,
    String descripcion,
    LocalDateTime inicio,
    LocalDateTime fin,
    EstadoReserva estado,
    LocalDateTime fechaCreacion
) {}
