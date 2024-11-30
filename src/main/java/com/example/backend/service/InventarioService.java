package com.example.backend.service;

import com.example.backend.dto.InventarioDTO;
import com.example.backend.model.Inventario;

import java.util.List;

public interface InventarioService {
    List<InventarioDTO> mostrarInventarios();

    List<InventarioDTO> buscarInventarioPorCriterios(String termino);

    Inventario crearInventario(InventarioDTO inventarioDTO);

    Inventario actualizarInventario(Long id, InventarioDTO inventarioDTO);

    void eliminarInventario(Long id);
}
