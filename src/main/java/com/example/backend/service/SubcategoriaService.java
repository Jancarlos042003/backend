package com.example.backend.service;

import com.example.backend.dto.SubcategoriaDTO;
import com.example.backend.model.Subcategoria;

import java.util.List;

public interface SubcategoriaService {
    Subcategoria crearSubcategoria(SubcategoriaDTO subcategoria);

    List<Subcategoria> mostrarSubcategorias();

    void eliminarSubategoria(Long id);
}
