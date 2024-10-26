package com.example.backend.service;

import com.example.backend.dto.LibroDTO;
import com.example.backend.model.Libro;

import java.util.List;

public interface LibroService {
    Libro crearLibro(LibroDTO libroDTO);

    List<Libro> mostrarLibros();

    Libro mostrarLibro(Long id);

    void eliminarLibro(Long id);

    Libro actualizarLibro(Long id, LibroDTO libroActualizado);
}
