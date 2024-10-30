package com.example.backend.controller;

import com.example.backend.dto.ReseniaDTO;
import com.example.backend.service.ReseniaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/resenia")
public class ReseniaController {
    @Autowired
    private ReseniaService reseniaService;

    @PostMapping("/{idUsuario}/{idLibro}")
    public ResponseEntity<?> crearResenia(@Valid @RequestBody ReseniaDTO reseniaDTO,@Min(1) @PathVariable Long idUsuario,@Min(1) @PathVariable Long idLibro){
        return new ResponseEntity<>(reseniaService.crearResenia(reseniaDTO, idLibro, idUsuario), HttpStatus.CREATED);
    }

    @PutMapping("/{idResenia}")
    public ResponseEntity<?> editarResenia(@Valid @RequestBody ReseniaDTO reseniaDTO, @Min(1) @PathVariable Long idResenia){
        return new ResponseEntity<>(reseniaService.editarResenia(reseniaDTO, idResenia), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResenia(@Min(1) @PathVariable Long id){
        reseniaService.eliminarResenia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarReseniasPorLibro(@Min(1) @PathVariable Long id){
        return new ResponseEntity<>(reseniaService.mostrarReseniaPorLibro(id), HttpStatus.OK);
    }
}
