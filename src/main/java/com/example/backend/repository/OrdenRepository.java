package com.example.backend.repository;

import com.example.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByIdOrdenPaypal (String ordePaypal);
}
