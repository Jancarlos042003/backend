package com.example.backend.controller;

import com.example.backend.model.Rol;
import com.example.backend.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> mostrarRoles(){
        return rolService.mostrarRoles();
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol){
        return new ResponseEntity<>(rolService.crearRol(rol), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id){
        rolService.eliminarRol(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
