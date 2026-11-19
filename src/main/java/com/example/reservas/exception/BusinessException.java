package com.example.reservas.exception;

public class BusinessException extends ApiException {
    public BusinessException(ErrorCode codigo) {
        super(codigo);
    }
    public BusinessException(ErrorCode codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
