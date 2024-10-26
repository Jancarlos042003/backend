package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDTO {
    @NotBlank(message = "Obligatorio pones el nombre de la Categoría.")
    private String nombre;
}
