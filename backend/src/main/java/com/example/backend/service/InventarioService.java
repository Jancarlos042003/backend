package com.example.backend.service;

import com.example.backend.model.Inventario;

import java.util.List;

public interface InventarioService {
    List<Inventario> mostrarInventarios();

    Inventario mostrarInventario(Long id);

    Inventario crearInventario(Inventario inventario);

    Inventario actualizarInventario(Long id, Inventario inventario);

    void eliminarInventario(Long id);
}
