package com.example.backend.controller;

import com.example.backend.dto.LibroCardDTO;
import com.example.backend.dto.LibroDTO;
import com.example.backend.model.Libro;
import com.example.backend.service.LibroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/libro")
public class LibroController {
    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public  ResponseEntity<List<LibroCardDTO>> mostrarLibros(){
        return new ResponseEntity<>(libroService.mostrarLibros(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> mostrarLibro(@Min(1) @PathVariable Long id){
        return new ResponseEntity<>(libroService.mostrarLibro(id), HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroCardDTO>> buscarLibrosPorCriterios(@RequestParam String termino){
        return new ResponseEntity<>(libroService.buscarLibrosPorCriterios(termino), HttpStatus.OK);
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<LibroCardDTO>> mostrarLibrosPorCategoria(@PathVariable Long id){
        return new ResponseEntity<>(libroService.obtenerLibrosPorCategoria(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody LibroDTO libroDTO){
        return new ResponseEntity<>(libroService.crearLibro(libroDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@Min(1) @PathVariable Long id,@Valid @RequestBody LibroDTO libroActualizado){
        libroService.actualizarLibro(id, libroActualizado);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@Min(1) @PathVariable Long id){
        libroService.eliminarLibro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
