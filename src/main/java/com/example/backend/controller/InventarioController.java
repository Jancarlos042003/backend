package com.example.backend.controller;

import com.example.backend.dto.InventarioDTO;
import com.example.backend.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    @Autowired
    InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<?> mostrarInventarios(){
        return new ResponseEntity<>(inventarioService.mostrarInventarios(), HttpStatus.OK);
    }

    @GetMapping("/{termino}")
    public ResponseEntity<?> buscarInventarioPorCriterios(@PathVariable String termino){
        return new ResponseEntity<>(inventarioService.buscarInventarioPorCriterios(termino), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearInventario(@RequestBody InventarioDTO inventario){
        return new ResponseEntity<>(inventarioService.crearInventario(inventario), HttpStatus.CREATED);
    }

    @PutMapping("/{idLibro}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long idLibro, @RequestBody InventarioDTO inventarioDTO){
        return new ResponseEntity<>(inventarioService.actualizarInventario(idLibro, inventarioDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable Long id){
        inventarioService.eliminarInventario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
