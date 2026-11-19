package com.example.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReservaRequestDTO(
    @NotNull(message = "salaId es obligatorio")
    Long salaId,

    @NotNull(message = "responsableId es obligatorio")
    Long responsableId,

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 200)
    String asunto,

    @Size(max = 500)
    String descripcion,

    @NotNull(message = "inicio es obligatorio")
    LocalDateTime inicio,

    @NotNull(message = "fin es obligatorio")
    LocalDateTime fin
) {}
