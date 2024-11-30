package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditorialDTO {
    private Long id;

    @NotBlank(message = "Obligatorio el nombre de la Editorial.")
    private String nombre;
}
