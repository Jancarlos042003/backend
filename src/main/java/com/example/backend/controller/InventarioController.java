package com.example.backend.controller;

import com.example.backend.dto.InventarioDTO;
import com.example.backend.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<InventarioDTO> mostrarInventarios(){
        return inventarioService.mostrarInventarios();
    }

    @GetMapping("/{termino}")
    public List<InventarioDTO> buscarInventarioPorCriterios(@PathVariable String termino){
        return inventarioService.buscarInventarioPorCriterios(termino);
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> crearInventario(@RequestBody InventarioDTO inventario){
        return new ResponseEntity<>(inventarioService.crearInventario(inventario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> actualizarInventario(@PathVariable Long id, @RequestBody InventarioDTO inventarioDTO){
        return new ResponseEntity<>(inventarioService.actualizarInventario(id, inventarioDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id){
        inventarioService.eliminarInventario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
