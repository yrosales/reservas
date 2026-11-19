package com.example.reservas.controller;

import com.example.reservas.dto.ReservaRequestDTO;
import com.example.reservas.dto.ReservaResponseDTO;
import com.example.reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;

    @GetMapping
    public List<ReservaResponseDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ReservaResponseDTO obtener(@PathVariable Long id) { return service.obtener(id); }

    @GetMapping("/sala/{salaId}")
    public List<ReservaResponseDTO> porSala(@PathVariable Long salaId) {
        return service.listarPorSala(salaId);
    }

    @GetMapping("/responsable/{responsableId}")
    public List<ReservaResponseDTO> porResponsable(@PathVariable Long responsableId) {
        return service.listarPorResponsable(responsableId);
    }

    @GetMapping("/disponible")
    public Map<String, Object> disponible(
            @RequestParam Long salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        boolean disponible = service.estaDisponible(salaId, inicio, fin, null);
        return Map.of(
                "salaId", salaId,
                "inicio", inicio,
                "fin", fin,
                "disponible", disponible
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponseDTO crear(@RequestBody @Valid ReservaRequestDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public ReservaResponseDTO actualizar(@PathVariable Long id,
                                         @RequestBody @Valid ReservaRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @PatchMapping("/{id}/cancelar")
    public ReservaResponseDTO cancelar(@PathVariable Long id) {
        return service.cancelar(id);
    }

    @PatchMapping("/{id}/finalizar")
    public ReservaResponseDTO finalizar(@PathVariable Long id) {
        return service.finalizar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
