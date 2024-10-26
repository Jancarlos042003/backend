package com.example.backend.service;

import com.example.backend.dto.AutorDTO;
import com.example.backend.model.Autor;

import java.util.List;

public interface AutorService {
    Autor crearAutor(AutorDTO autor);

    List<Autor> mostrarAutores();

    void eliminarAutor(Long id);
}
