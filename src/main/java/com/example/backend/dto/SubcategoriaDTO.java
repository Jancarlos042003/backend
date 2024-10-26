package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubcategoriaDTO {
    @NotBlank(message = "Obligatorio poner el nombre de la Categoría.")
    private String nombre;

    @NotNull(message = "El ID de la categoría es obligatorio.")
    private Long id;
}
