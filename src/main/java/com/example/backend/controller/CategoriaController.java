package com.example.backend.controller;

import com.example.backend.dto.CategoriaDTO;
import com.example.backend.model.Categoria;
import com.example.backend.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> mostrarCategorias(){
        return categoriaService.mostrarCategorias();
    }

    @GetMapping("/{id}")
    public Categoria mostrarCategoria(@PathVariable Long id){
        return categoriaService.mostrarCategoria(id);
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody CategoriaDTO categoria){
        return new ResponseEntity<>(categoriaService.crearCategoria(categoria), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@Min(1) @PathVariable Long id){
        categoriaService.eliminarCategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
