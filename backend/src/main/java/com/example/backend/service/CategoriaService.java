package com.example.backend.service;

import com.example.backend.dto.CategoriaDTO;
import com.example.backend.model.Categoria;

import java.util.List;

public interface CategoriaService {
    Categoria crearCategoria(CategoriaDTO categoria);

    List<Categoria> mostrarCategorias();

    void eliminarCategoria(Long id);
}
