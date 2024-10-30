package com.example.backend.repository;

import com.example.backend.model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
    Set<Resenia> findAllByLibroId(Long idLibro);
}
