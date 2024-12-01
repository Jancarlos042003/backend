package com.example.backend.service;

import com.example.backend.dto.LibroBusquedaDTO;
import com.example.backend.dto.LibroCardDTO;
import com.example.backend.dto.LibroDTO;
import com.example.backend.model.Libro;

import java.util.List;

public interface LibroService {
    Libro crearLibro(LibroDTO libroDTO);

    List<LibroCardDTO> mostrarLibros();

    Libro mostrarLibro(Long id);

    void eliminarLibro(Long id);

    Libro actualizarLibro(Long id, LibroDTO libroActualizado);

    List<LibroBusquedaDTO> buscarLibrosPorCriterios(String termino);

    List<LibroDTO> mostrarLibrosPorCategoria(Long id);

    List<LibroCardDTO> obtenerLibrosPorCategoria(Long categoriaId);
}
