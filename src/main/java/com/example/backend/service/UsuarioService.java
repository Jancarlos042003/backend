package com.example.backend.service;

import com.example.backend.dto.CrearUsuarioDTO;
import com.example.backend.model.Usuario;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UsuarioService {
    Usuario crearUsuario(CrearUsuarioDTO crearUsuarioDTO);

    List<Usuario> mostrarUsuarios();

    Usuario mostrarUsuario(Long id);

    Usuario mostrarUsuarioAutenticado (Authentication authentication);

    Usuario actualizarUsuario(Long id, CrearUsuarioDTO usuarioActualizado);

    void eliminarUsuario(Long id);
}
