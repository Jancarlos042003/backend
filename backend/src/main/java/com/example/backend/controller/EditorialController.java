package com.example.backend.controller;

import com.example.backend.dto.EditorialDTO;
import com.example.backend.service.EditorialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/editorial")
public class EditorialController {
    @Autowired
    EditorialService editorialService;

    @GetMapping
    public ResponseEntity<?> mostrarEditorial(){
        return new ResponseEntity<>(editorialService.mostrarEditoriales(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearEditorial(@Valid @RequestBody EditorialDTO editorial){
        return new ResponseEntity<>(editorialService.crearEditorial(editorial), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEditorial(@Min(1) @PathVariable Long id){
        editorialService.eliminarEditorial(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
