package com.example.backend.service;

import com.example.backend.dto.EditorialDTO;
import com.example.backend.model.Editorial;

import java.util.List;

public interface EditorialService {
    Editorial crearEditorial(EditorialDTO editorial);

    List<Editorial> mostrarEditoriales();

    void eliminarEditorial(Long id);
}
