package com.example.backend.service;

import com.example.backend.dto.ReseniaDTO;
import com.example.backend.model.Resenia;

import java.util.Set;

public interface ReseniaService {
    Resenia crearResenia(ReseniaDTO reseniaDTO, Long idLibro, Long idUsuario);

    Resenia editarResenia(ReseniaDTO reseniaDTO, Long idResenia);

    void eliminarResenia(Long idResenia);

    Set<Resenia> mostrarReseniaPorLibro(Long idLibro);
}
