package com.example.backend.controller;

import com.example.backend.dto.SubcategoriaDTO;
import com.example.backend.service.SubcategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/subcategoria")
public class SubcategoriaController {
    @Autowired
    SubcategoriaService subcategoriaService;

    @GetMapping
    public ResponseEntity<?> mostrarSubcategorias(){
        return new ResponseEntity<>(subcategoriaService.mostrarSubcategorias(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearSubcategoria(@Valid @RequestBody SubcategoriaDTO subcategoria){
        return new ResponseEntity<>(subcategoriaService.crearSubcategoria(subcategoria), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSubcategoria(@Min(1) @PathVariable Long id){
        subcategoriaService.eliminarSubategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
