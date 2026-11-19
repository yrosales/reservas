package com.example.reservas.exception;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(ErrorCode codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
