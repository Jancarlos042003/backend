package com.example.backend.service;

import com.example.backend.dto.CrearUsuarioDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Rol;
import com.example.backend.model.Usuario;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Usuario crearUsuario(CrearUsuarioDTO crearUsuarioDTO){
        Rol rol;

        if (crearUsuarioDTO.getRol() == null){
            rol = rolRepository.findByNombre("USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Rol USER no encontrado."));
        } else if(crearUsuarioDTO.getRol() != null && crearUsuarioDTO.getRol().equalsIgnoreCase("ADMIN")) {
            rol = rolRepository.findByNombre("ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException("Rol ADMIN no encontrado."));
        } else {
            throw new IllegalArgumentException("Rol no válido");
        }


        Usuario usuario = Usuario.builder()
                .nombreCompleto(crearUsuarioDTO.getNombreCompleto())
                .username(crearUsuarioDTO.getUsername())
                .contrasenia(passwordEncoder.encode(crearUsuarioDTO.getContrasenia()))
                .rol(rol)
                .fechaRegistro(LocalDate.now())
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> mostrarUsuarios(){
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario mostrarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return usuario;
    }

    @Transactional
    @Override
    public Usuario actualizarUsuario(Long id, CrearUsuarioDTO usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    if (usuarioActualizado.getNombreCompleto() != null) {
                        usuarioExistente.setNombreCompleto(usuarioActualizado.getNombreCompleto());
                    }
                    if (usuarioActualizado.getUsername() != null) {
                        usuarioExistente.setUsername(usuarioActualizado.getUsername());
                    }
                    if (usuarioActualizado.getContrasenia() != null) {
                        String contraseniaEncriptada = passwordEncoder.encode(usuarioActualizado.getContrasenia());
                        usuarioExistente.setContrasenia(contraseniaEncriptada);
                    }
                    return usuarioRepository.save(usuarioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el ID: " + id));
    }

    @Override
    public void eliminarUsuario(Long id){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()){
            usuarioRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Usuario no encontrado con el ID: " + id);
        }

    }
}
