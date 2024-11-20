package com.example.backend.repository;

import com.example.backend.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l WHERE " +
            "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(l.autor) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Libro> buscarPorCriterios(@Param("termino") String termino);
}
