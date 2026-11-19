package com.example.reservas.repository;

import com.example.reservas.entity.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {

    List<Responsable> findByActivoTrue();

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
