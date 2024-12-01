package com.example.backend.repository;

import com.example.backend.dto.LibroCardDTO;
import com.example.backend.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l WHERE " +
            "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(l.autor) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Libro> buscarPorCriterios(@Param("termino") String termino);

    Optional<List<Libro>> findByCategoriasId(Long id);

    @Query("SELECT new com.example.backend.dto.LibroCardDTO(l.id, l.titulo, l.autor, l.isbn, l.precio, l.descuento, l.descripcion, l.imgPortada) " +
            "FROM Libro l " +
            "JOIN l.categorias c " +
            "WHERE c.id = :categoriaId")
    List<LibroCardDTO> findLibroCardsByCategoriaId(@Param("categoriaId") Long categoriaId);

}
