package com.example.backend.controller;

import com.example.backend.dto.EditorialDTO;
import com.example.backend.model.Editorial;
import com.example.backend.service.EditorialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/editorial")
public class EditorialController {
    private final EditorialService editorialService;

    public EditorialController(EditorialService editorialService) {
        this.editorialService = editorialService;
    }

    @GetMapping
    public ResponseEntity<List<Editorial>> mostrarEditoriales(){
        return new ResponseEntity<>(editorialService.mostrarEditoriales(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Editorial> crearEditorial(@Valid @RequestBody EditorialDTO editorial){
        return new ResponseEntity<>(editorialService.crearEditorial(editorial), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEditorial(@Min(1) @PathVariable Long id){
        editorialService.eliminarEditorial(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
