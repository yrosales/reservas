package com.example.reservas.exception;

import com.example.reservas.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> apiException(ApiException ex, HttpServletRequest req) {
        return build(ex.getCodigo(), ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return build(ErrorCode.VALIDACION, msg, req);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorResponse> badRequest(Exception ex, HttpServletRequest req) {
        return build(ErrorCode.SOLICITUD_INVALIDA, ex.getMessage(), req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        String causa = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage() : "";
        if (causa != null && causa.toLowerCase().contains("excl_reserva_solapada")) {
            return build(ErrorCode.SALA_OCUPADA,
                    "La sala ya tiene una reserva activa en ese horario", req);
        }
        return build(ErrorCode.INTEGRIDAD_DATOS, "Violación de integridad de datos", req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception ex, HttpServletRequest req) {
        return build(ErrorCode.ERROR_INTERNO, ex.getMessage(), req);
    }

    private ResponseEntity<ErrorResponse> build(ErrorCode codigo, String mensaje, HttpServletRequest req) {
        return ResponseEntity.status(codigo.getStatus()).body(new ErrorResponse(
                Instant.now(),
                codigo.getStatus().value(),
                codigo.name(),
                codigo.getStatus().getReasonPhrase(),
                mensaje,
                req.getRequestURI()));
    }
}
