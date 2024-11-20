package com.example.backend.controller;

import com.example.backend.dto.LibroBusquedaDTO;
import com.example.backend.dto.LibroDTO;
import com.example.backend.service.LibroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/libro")
public class LibroController {
    @Autowired
    LibroService libroService;

    @GetMapping
    public  ResponseEntity<?> mostrarLibros(){
        return new ResponseEntity<>(libroService.mostrarLibros(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarLibro(@Min(1) @PathVariable Long id){
        return new ResponseEntity<>(libroService.mostrarLibro(id), HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroBusquedaDTO>> buscarLibrosPorCriterios(@RequestParam String termino){
        return new ResponseEntity<>(libroService.buscarLibrosPorCriterios(termino), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@Valid @RequestBody LibroDTO libroDTO){
        return new ResponseEntity<>(libroService.crearLibro(libroDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarLibro(@Min(1) @PathVariable Long id,@Valid @RequestBody LibroDTO libroActualizado){
        libroService.actualizarLibro(id, libroActualizado);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@Min(1) @PathVariable Long id){
        libroService.eliminarLibro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
