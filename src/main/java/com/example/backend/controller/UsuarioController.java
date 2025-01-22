package com.example.backend.controller;

import com.example.backend.dto.CrearUsuarioDTO;
import com.example.backend.model.Usuario;
import com.example.backend.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> mostrarUsuarios(){
        return new ResponseEntity<>(usuarioService.mostrarUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> mostrarUsuarioAutenticado(Authentication authentication) {
        // Llama al servicio para obtener el usuario autenticado
        Usuario usuario = usuarioService.mostrarUsuarioAutenticado(authentication);
        return new ResponseEntity<>(usuario, HttpStatus.OK);  // Devuelve el usuario autenticado en la respuesta
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO){
        return new ResponseEntity<>(usuarioService.crearUsuario(crearUsuarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@Min(1) @PathVariable Long id,@Valid @RequestBody CrearUsuarioDTO usuarioActualizado){
        return new ResponseEntity<>(usuarioService.actualizarUsuario(id, usuarioActualizado), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@Min(1) @PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
