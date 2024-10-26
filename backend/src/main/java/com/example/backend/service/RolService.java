package com.example.backend.service;

import com.example.backend.model.Rol;

import java.util.List;

public interface RolService {
    Rol crearRol(Rol rol);

    void eliminarRol(Long id);

    List<Rol> mostrarRoles();
}
