package com.example.backend.repository;

import com.example.backend.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    Optional<List<DetalleOrden>> findAllByIdOrdenPaypal(String idOrdenPaypal);
}
