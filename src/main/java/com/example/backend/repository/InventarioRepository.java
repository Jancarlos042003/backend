package com.example.backend.repository;

import com.example.backend.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    @Query("SELECT i FROM Inventario i " +
            "JOIN i.libro l " +
            "WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(l.autor) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Inventario> buscarPorCriterios(@Param("termino") String termino);

    Optional<Inventario> findByLibroId(Long id);
}
