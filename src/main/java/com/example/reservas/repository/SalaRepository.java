package com.example.reservas.repository;

import com.example.reservas.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByActivaTrue();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
