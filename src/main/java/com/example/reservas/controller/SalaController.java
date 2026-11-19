package com.example.reservas.controller;

import com.example.reservas.dto.SalaDTO;
import com.example.reservas.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService service;

    @GetMapping
    public List<SalaDTO> listar() { return service.listar(); }

    @GetMapping("/activas")
    public List<SalaDTO> activas() { return service.listarActivas(); }

    @GetMapping("/{id}")
    public SalaDTO obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalaDTO crear(@RequestBody @Valid SalaDTO dto) { return service.crear(dto); }

    @PutMapping("/{id}")
    public SalaDTO actualizar(@PathVariable Long id, @RequestBody @Valid SalaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
