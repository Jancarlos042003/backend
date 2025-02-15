package com.example.backend.controller;

import com.example.backend.dto.SubcategoriaDTO;
import com.example.backend.model.Subcategoria;
import com.example.backend.service.SubcategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/subcategoria")
public class SubcategoriaController {
    private final SubcategoriaService subcategoriaService;

    public SubcategoriaController(SubcategoriaService subcategoriaService) {
        this.subcategoriaService = subcategoriaService;
    }

    @GetMapping
    public List<Subcategoria> mostrarSubcategorias(){
        return subcategoriaService.mostrarSubcategorias();
    }

    @PostMapping
    public ResponseEntity<Subcategoria> crearSubcategoria(@Valid @RequestBody SubcategoriaDTO subcategoria){
        return new ResponseEntity<>(subcategoriaService.crearSubcategoria(subcategoria), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubcategoria(@Min(1) @PathVariable Long id){
        subcategoriaService.eliminarSubategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
