package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditorialDTO {
    @NotBlank(message = "Obligatorio el nombre de la Editorial.")
    private String nombre;
}
