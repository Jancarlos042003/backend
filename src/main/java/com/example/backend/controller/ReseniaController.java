package com.example.backend.controller;

import com.example.backend.dto.ReseniaDTO;
import com.example.backend.model.Resenia;
import com.example.backend.service.ReseniaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Validated
@RestController
@RequestMapping("/api/resenia")
public class ReseniaController {
    private final ReseniaService reseniaService;

    public ReseniaController(ReseniaService reseniaService) {
        this.reseniaService = reseniaService;
    }

    @PostMapping("/{idUsuario}/{idLibro}")
    public ResponseEntity<Resenia> crearResenia(@Valid @RequestBody ReseniaDTO reseniaDTO, @Min(1) @PathVariable Long idUsuario, @Min(1) @PathVariable Long idLibro){
        return new ResponseEntity<>(reseniaService.crearResenia(reseniaDTO, idLibro, idUsuario), HttpStatus.CREATED);
    }

    @PutMapping("/{idResenia}")
    public ResponseEntity<Resenia> editarResenia(@Valid @RequestBody ReseniaDTO reseniaDTO, @Min(1) @PathVariable Long idResenia){
        return new ResponseEntity<>(reseniaService.editarResenia(reseniaDTO, idResenia), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResenia(@Min(1) @PathVariable Long id){
        reseniaService.eliminarResenia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<Resenia>> mostrarReseniasPorLibro(@Min(1) @PathVariable Long id){
        return new ResponseEntity<>(reseniaService.mostrarReseniaPorLibro(id), HttpStatus.OK);
    }
}
