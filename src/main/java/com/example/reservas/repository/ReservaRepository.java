package com.example.reservas.repository;

import com.example.reservas.entity.EstadoReserva;
import com.example.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findBySalaId(Long salaId);

    List<Reserva> findByResponsableId(Long responsableId);

    List<Reserva> findByEstado(EstadoReserva estado);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.estado = 'ACTIVA'
          AND r.id <> :excludeId
          AND r.inicio < :fin
          AND r.fin > :inicio
    """)
    boolean existeSolapamiento(@Param("salaId") Long salaId,
                               @Param("inicio") LocalDateTime inicio,
                               @Param("fin") LocalDateTime fin,
                               @Param("excludeId") Long excludeId);

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.estado = 'ACTIVA'
          AND r.inicio < :fin
          AND r.fin > :inicio
        ORDER BY r.inicio
    """)
    List<Reserva> findActivasEnRango(@Param("salaId") Long salaId,
                                     @Param("inicio") LocalDateTime inicio,
                                     @Param("fin") LocalDateTime fin);
}
