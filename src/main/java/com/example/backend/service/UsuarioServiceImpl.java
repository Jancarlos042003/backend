package com.example.backend.service;

import com.example.backend.dto.CrearUsuarioDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Rol;
import com.example.backend.model.Usuario;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

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
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Override
    public Usuario mostrarUsuarioAutenticado(Authentication authentication) {
        // No hacer el cast directo, sino obtener el username y buscar el usuario
        String username = authentication.getName(); // Obtiene el username del usuario autenticado
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    @Override
    public Usuario actualizarUsuario(Long id, CrearUsuarioDTO usuario) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el ID: " + id));

        usuarioEncontrado.setNombreCompleto(usuario.getNombreCompleto());
        usuarioEncontrado.setUsername(usuario.getUsername());
        usuarioEncontrado.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

        return usuarioRepository.save(usuarioEncontrado);
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
