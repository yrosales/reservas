package com.example.reservas.exception;

public class ApiException extends RuntimeException {

    private final ErrorCode codigo;

    public ApiException(ErrorCode codigo) {
        super(codigo.getMensajePorDefecto());
        this.codigo = codigo;
    }

    public ApiException(ErrorCode codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public ErrorCode getCodigo() { return codigo; }
}
