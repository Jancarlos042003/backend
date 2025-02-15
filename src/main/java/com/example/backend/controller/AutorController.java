package com.example.backend.controller;

import com.example.backend.dto.AutorDTO;
import com.example.backend.model.Autor;
import com.example.backend.service.AutorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/autor")
public class AutorController {
    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public List<Autor> mostrarAutores(){
        return autorService.mostrarAutores();
    }

    @PostMapping
    public ResponseEntity<Autor> crearAutor(@Valid @RequestBody AutorDTO autor){
        return new ResponseEntity<>(autorService.crearAutor(autor), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(@Min(1) @PathVariable Long id){
        autorService.eliminarAutor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
