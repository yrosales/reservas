package com.example.reservas.service;

import com.example.reservas.dto.ResponsableDTO;
import com.example.reservas.dto.ReservaRequestDTO;
import com.example.reservas.dto.ReservaResponseDTO;
import com.example.reservas.dto.SalaDTO;
import com.example.reservas.entity.EstadoReserva;
import com.example.reservas.entity.Reserva;
import com.example.reservas.entity.Responsable;
import com.example.reservas.entity.Sala;
import com.example.reservas.exception.BusinessException;
import com.example.reservas.exception.ErrorCode;
import com.example.reservas.exception.ResourceNotFoundException;
import com.example.reservas.exception.SalaOcupadaException;
import com.example.reservas.repository.ReservaRepository;
import com.example.reservas.repository.ResponsableRepository;
import com.example.reservas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final SalaRepository salaRepo;
    private final ResponsableRepository responsableRepo;

    public List<ReservaResponseDTO> listar() {
        return reservaRepo.findAll().stream().map(this::toDTO).toList();
    }

    public ReservaResponseDTO obtener(Long id) {
        return toDTO(findOrThrow(id));
    }

    public List<ReservaResponseDTO> listarPorSala(Long salaId) {
        return reservaRepo.findBySalaId(salaId).stream().map(this::toDTO).toList();
    }

    public List<ReservaResponseDTO> listarPorResponsable(Long responsableId) {
        return reservaRepo.findByResponsableId(responsableId).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public boolean estaDisponible(Long salaId, LocalDateTime inicio, LocalDateTime fin, Long excludeId) {
        validarFechas(inicio, fin);
        return !reservaRepo.existeSolapamiento(salaId, inicio, fin,
                excludeId == null ? -1L : excludeId);
    }

    public ReservaResponseDTO crear(ReservaRequestDTO dto) {
        validarFechas(dto.inicio(), dto.fin());

        Sala sala = salaRepo.findById(dto.salaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.SALA_NO_ENCONTRADA, "Sala no encontrada con id " + dto.salaId()));
        if (!sala.getActiva())
            throw new BusinessException(ErrorCode.SALA_INACTIVA, "La sala está inactiva");

        Responsable resp = responsableRepo.findById(dto.responsableId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESPONSABLE_NO_ENCONTRADO,
                        "Responsable no encontrado con id " + dto.responsableId()));
        if (!resp.getActivo())
            throw new BusinessException(ErrorCode.RESPONSABLE_INACTIVO, "El responsable está inactivo");

        if (reservaRepo.existeSolapamiento(sala.getId(), dto.inicio(), dto.fin(), -1L))
            throw new SalaOcupadaException(
                    "La sala '" + sala.getNombre() + "' ya está ocupada en el horario solicitado");

        Reserva r = new Reserva();
        r.setSala(sala);
        r.setResponsable(resp);
        r.setAsunto(dto.asunto());
        r.setDescripcion(dto.descripcion());
        r.setInicio(dto.inicio());
        r.setFin(dto.fin());
        r.setEstado(EstadoReserva.ACTIVA);

        return toDTO(reservaRepo.save(r));
    }

    public ReservaResponseDTO actualizar(Long id, ReservaRequestDTO dto) {
        Reserva r = findOrThrow(id);
        if (r.getEstado() == EstadoReserva.CANCELADA || r.getEstado() == EstadoReserva.FINALIZADA)
            throw new BusinessException(ErrorCode.RESERVA_NO_MODIFICABLE,
                    "No se puede modificar una reserva " + r.getEstado());

        validarFechas(dto.inicio(), dto.fin());

        Sala sala = salaRepo.findById(dto.salaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.SALA_NO_ENCONTRADA, "Sala no encontrada con id " + dto.salaId()));
        Responsable resp = responsableRepo.findById(dto.responsableId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESPONSABLE_NO_ENCONTRADO,
                        "Responsable no encontrado con id " + dto.responsableId()));

        if (reservaRepo.existeSolapamiento(sala.getId(), dto.inicio(), dto.fin(), id))
            throw new SalaOcupadaException(
                    "La sala '" + sala.getNombre() + "' ya está ocupada en el horario solicitado");

        r.setSala(sala);
        r.setResponsable(resp);
        r.setAsunto(dto.asunto());
        r.setDescripcion(dto.descripcion());
        r.setInicio(dto.inicio());
        r.setFin(dto.fin());

        return toDTO(reservaRepo.save(r));
    }

    public ReservaResponseDTO cancelar(Long id) {
        Reserva r = findOrThrow(id);
        if (r.getEstado() != EstadoReserva.ACTIVA)
            throw new BusinessException(ErrorCode.RESERVA_NO_CANCELABLE,
                    "Solo se puede cancelar una reserva ACTIVA");
        r.setEstado(EstadoReserva.CANCELADA);
        return toDTO(reservaRepo.save(r));
    }

    public ReservaResponseDTO finalizar(Long id) {
        Reserva r = findOrThrow(id);
        if (r.getEstado() != EstadoReserva.ACTIVA)
            throw new BusinessException(ErrorCode.RESERVA_NO_FINALIZABLE,
                    "Solo se puede finalizar una reserva ACTIVA");
        r.setEstado(EstadoReserva.FINALIZADA);
        return toDTO(reservaRepo.save(r));
    }

    public void eliminar(Long id) {
        reservaRepo.delete(findOrThrow(id));
    }

    private void validarFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null)
            throw new BusinessException(ErrorCode.FECHAS_INVALIDAS,
                    "inicio y fin son obligatorios");
        if (!fin.isAfter(inicio))
            throw new BusinessException(ErrorCode.FECHAS_INVALIDAS,
                    "La fecha/hora de fin debe ser posterior a la de inicio");
    }

    private Reserva findOrThrow(Long id) {
        return reservaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESERVA_NO_ENCONTRADA,
                        "Reserva no encontrada con id " + id));
    }

    private ReservaResponseDTO toDTO(Reserva r) {
        SalaDTO salaDto = new SalaDTO(r.getSala().getId(), r.getSala().getNombre(),
                r.getSala().getUbicacion(), r.getSala().getCapacidad(),
                r.getSala().getDescripcion(), r.getSala().getActiva());

        ResponsableDTO respDto = new ResponsableDTO(r.getResponsable().getId(),
                r.getResponsable().getNombre(), r.getResponsable().getApellido(),
                r.getResponsable().getEmail(), r.getResponsable().getTelefono(),
                r.getResponsable().getCargo(), r.getResponsable().getActivo());

        return new ReservaResponseDTO(r.getId(), salaDto, respDto, r.getAsunto(),
                r.getDescripcion(), r.getInicio(), r.getFin(), r.getEstado(), r.getFechaCreacion());
    }
}
