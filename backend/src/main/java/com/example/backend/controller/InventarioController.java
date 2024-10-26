package com.example.backend.controller;

import com.example.backend.model.Inventario;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarInventario(@PathVariable Long id){
        return new ResponseEntity<>(inventarioService.mostrarInventario(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearInventario(@RequestBody Inventario inventario){
        return new ResponseEntity<>(inventarioService.crearInventario(inventario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long id, @RequestBody Inventario inventario){
        return new ResponseEntity<>(inventarioService.actualizarInventario(id, inventario), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable Long id){
        inventarioService.eliminarInventario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
