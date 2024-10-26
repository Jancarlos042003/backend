package com.example.backend.controller;

import com.example.backend.model.Rol;
import com.example.backend.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rol")
public class RolController {
    @Autowired
    RolService rolService;

    @GetMapping
    public ResponseEntity<?> mostrarRoles(){
        return new ResponseEntity<>(rolService.mostrarRoles(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody Rol rol){
        return new ResponseEntity<>(rolService.crearRol(rol), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id){
        rolService.eliminarRol(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
