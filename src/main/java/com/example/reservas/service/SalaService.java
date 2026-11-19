package com.example.reservas.service;

import com.example.reservas.dto.SalaDTO;
import com.example.reservas.entity.Sala;
import com.example.reservas.exception.BusinessException;
import com.example.reservas.exception.ErrorCode;
import com.example.reservas.exception.ResourceNotFoundException;
import com.example.reservas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaService {

    private final SalaRepository salaRepo;

    public List<SalaDTO> listar() {
        return salaRepo.findAll().stream().map(this::toDTO).toList();
    }

    public List<SalaDTO> listarActivas() {
        return salaRepo.findByActivaTrue().stream().map(this::toDTO).toList();
    }

    public SalaDTO obtener(Long id) {
        return toDTO(findOrThrow(id));
    }

    public SalaDTO crear(SalaDTO dto) {
        if (salaRepo.existsByNombreIgnoreCase(dto.nombre()))
            throw new BusinessException(ErrorCode.SALA_DUPLICADA,
                    "Ya existe una sala con el nombre: " + dto.nombre());
        Sala s = new Sala();
        aplicar(s, dto);
        return toDTO(salaRepo.save(s));
    }

    public SalaDTO actualizar(Long id, SalaDTO dto) {
        Sala s = findOrThrow(id);
        if (salaRepo.existsByNombreIgnoreCaseAndIdNot(dto.nombre(), id))
            throw new BusinessException(ErrorCode.SALA_DUPLICADA,
                    "Ya existe otra sala con el nombre: " + dto.nombre());
        aplicar(s, dto);
        return toDTO(salaRepo.save(s));
    }

    public void eliminar(Long id) {
        salaRepo.delete(findOrThrow(id));
    }

    private Sala findOrThrow(Long id) {
        return salaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.SALA_NO_ENCONTRADA, "Sala no encontrada con id " + id));
    }

    private void aplicar(Sala s, SalaDTO dto) {
        s.setNombre(dto.nombre());
        s.setUbicacion(dto.ubicacion());
        s.setCapacidad(dto.capacidad());
        s.setDescripcion(dto.descripcion());
        if (dto.activa() != null) s.setActiva(dto.activa());
    }

    private SalaDTO toDTO(Sala s) {
        return new SalaDTO(s.getId(), s.getNombre(), s.getUbicacion(),
                s.getCapacidad(), s.getDescripcion(), s.getActiva());
    }
}
