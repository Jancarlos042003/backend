package com.example.backend.controller;

import com.example.backend.dto.AutorDTO;
import com.example.backend.service.AutorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/autor")
public class AutorController {
    @Autowired
    AutorService autorService;

    @GetMapping
    public ResponseEntity<?> mostrarAutores(){
        return new ResponseEntity<>(autorService.mostrarAutores(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearAutor(@Valid @RequestBody AutorDTO autor){
        return new ResponseEntity<>(autorService.crearAutor(autor), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAutor(@Min(1) @PathVariable Long id){
        autorService.eliminarAutor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
