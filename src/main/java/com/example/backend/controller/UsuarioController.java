package com.example.backend.controller;

import com.example.backend.dto.CrearUsuarioDTO;
import com.example.backend.model.Usuario;
import com.example.backend.repository.RolRepository;
import com.example.backend.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> mostrarUsuarios(){
        return new ResponseEntity<>(usuarioService.mostrarUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> mostrarUsuarioAutenticado(Authentication authentication) {
        // Llama al servicio para obtener el usuario autenticado
        Usuario usuario = usuarioService.mostrarUsuarioAutenticado(authentication);
        return new ResponseEntity<>(usuario, HttpStatus.OK);  // Devuelve el usuario autenticado en la respuesta
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO){
        return new ResponseEntity<>(usuarioService.crearUsuario(crearUsuarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@Min(1) @PathVariable Long id,@Valid @RequestBody CrearUsuarioDTO usuarioActualizado){
        return new ResponseEntity<>(usuarioService.actualizarUsuario(id, usuarioActualizado), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@Min(1) @PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
