package com.example.reservas.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 409 Conflict
    SALA_OCUPADA(HttpStatus.CONFLICT, "La sala ya está ocupada en el horario solicitado"),

    // 404 Not Found
    SALA_NO_ENCONTRADA(HttpStatus.NOT_FOUND, "Sala no encontrada"),
    RESPONSABLE_NO_ENCONTRADO(HttpStatus.NOT_FOUND, "Responsable no encontrado"),
    RESERVA_NO_ENCONTRADA(HttpStatus.NOT_FOUND, "Reserva no encontrada"),

    // 400 Bad Request
    FECHAS_INVALIDAS(HttpStatus.BAD_REQUEST, "Las fechas de la reserva no son válidas"),
    SALA_INACTIVA(HttpStatus.BAD_REQUEST, "La sala está inactiva"),
    RESPONSABLE_INACTIVO(HttpStatus.BAD_REQUEST, "El responsable está inactivo"),
    RESERVA_NO_MODIFICABLE(HttpStatus.BAD_REQUEST, "La reserva no se puede modificar en su estado actual"),
    RESERVA_NO_CANCELABLE(HttpStatus.BAD_REQUEST, "La reserva no se puede cancelar en su estado actual"),
    RESERVA_NO_FINALIZABLE(HttpStatus.BAD_REQUEST, "La reserva no se puede finalizar en su estado actual"),
    SALA_DUPLICADA(HttpStatus.BAD_REQUEST, "Ya existe una sala con ese nombre"),
    RESPONSABLE_DUPLICADO(HttpStatus.BAD_REQUEST, "Ya existe un responsable con ese email"),
    VALIDACION(HttpStatus.BAD_REQUEST, "Error de validación en los datos enviados"),
    SOLICITUD_INVALIDA(HttpStatus.BAD_REQUEST, "Solicitud inválida"),
    INTEGRIDAD_DATOS(HttpStatus.BAD_REQUEST, "Violación de integridad de datos"),

    // 500
    ERROR_INTERNO(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");

    private final HttpStatus status;
    private final String mensajePorDefecto;

    ErrorCode(HttpStatus status, String mensajePorDefecto) {
        this.status = status;
        this.mensajePorDefecto = mensajePorDefecto;
    }

    public HttpStatus getStatus() { return status; }
    public String getMensajePorDefecto() { return mensajePorDefecto; }
}
