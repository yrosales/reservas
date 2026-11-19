package com.example.reservas.exception;

public class SalaOcupadaException extends ApiException {
    public SalaOcupadaException() {
        super(ErrorCode.SALA_OCUPADA);
    }
    public SalaOcupadaException(String mensaje) {
        super(ErrorCode.SALA_OCUPADA, mensaje);
    }
}
