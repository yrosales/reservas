package com.example.reservas.controller;

import com.example.reservas.dto.ResponsableDTO;
import com.example.reservas.service.ResponsableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responsables")
@RequiredArgsConstructor
public class ResponsableController {

    private final ResponsableService service;

    @GetMapping
    public List<ResponsableDTO> listar() { return service.listar(); }

    @GetMapping("/activos")
    public List<ResponsableDTO> activos() { return service.listarActivos(); }

    @GetMapping("/{id}")
    public ResponsableDTO obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsableDTO crear(@RequestBody @Valid ResponsableDTO dto) { return service.crear(dto); }

    @PutMapping("/{id}")
    public ResponsableDTO actualizar(@PathVariable Long id, @RequestBody @Valid ResponsableDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
