package com.example.reservas.service;

import com.example.reservas.dto.ResponsableDTO;
import com.example.reservas.entity.Responsable;
import com.example.reservas.exception.BusinessException;
import com.example.reservas.exception.ErrorCode;
import com.example.reservas.exception.ResourceNotFoundException;
import com.example.reservas.repository.ResponsableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponsableService {

    private final ResponsableRepository repo;

    public List<ResponsableDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public List<ResponsableDTO> listarActivos() {
        return repo.findByActivoTrue().stream().map(this::toDTO).toList();
    }

    public ResponsableDTO obtener(Long id) {
        return toDTO(findOrThrow(id));
    }

    public ResponsableDTO crear(ResponsableDTO dto) {
        if (repo.existsByEmailIgnoreCase(dto.email()))
            throw new BusinessException(ErrorCode.RESPONSABLE_DUPLICADO,
                    "Ya existe un responsable con email: " + dto.email());
        Responsable r = new Responsable();
        aplicar(r, dto);
        return toDTO(repo.save(r));
    }

    public ResponsableDTO actualizar(Long id, ResponsableDTO dto) {
        Responsable r = findOrThrow(id);
        if (repo.existsByEmailIgnoreCaseAndIdNot(dto.email(), id))
            throw new BusinessException(ErrorCode.RESPONSABLE_DUPLICADO,
                    "Ya existe otro responsable con email: " + dto.email());
        aplicar(r, dto);
        return toDTO(repo.save(r));
    }

    public void eliminar(Long id) {
        repo.delete(findOrThrow(id));
    }

    private Responsable findOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESPONSABLE_NO_ENCONTRADO,
                        "Responsable no encontrado con id " + id));
    }

    private void aplicar(Responsable r, ResponsableDTO dto) {
        r.setNombre(dto.nombre());
        r.setApellido(dto.apellido());
        r.setEmail(dto.email());
        r.setTelefono(dto.telefono());
        r.setCargo(dto.cargo());
        if (dto.activo() != null) r.setActivo(dto.activo());
    }

    private ResponsableDTO toDTO(Responsable r) {
        return new ResponsableDTO(r.getId(), r.getNombre(), r.getApellido(),
                r.getEmail(), r.getTelefono(), r.getCargo(), r.getActivo());
    }
}
