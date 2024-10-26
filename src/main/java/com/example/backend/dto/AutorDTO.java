package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AutorDTO {
    @NotBlank(message = "Obligatorio el nombre del Autor.")
    private String nombre;
}