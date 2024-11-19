package com.example.backend.controller;

import com.example.backend.dto.CategoriaDTO;
import com.example.backend.model.Categoria;
import com.example.backend.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> mostrarCategorias(){
        return new ResponseEntity<>(categoriaService.mostrarCategorias(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> mostrarCategoria(@PathVariable Long id){
        return new ResponseEntity<>(categoriaService.mostrarCategoria(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaDTO categoria){
        return new ResponseEntity<>(categoriaService.crearCategoria(categoria), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@Min(1) @PathVariable Long id){
        categoriaService.eliminarCategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
