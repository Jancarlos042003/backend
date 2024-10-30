package com.example.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReseniaDTO {
    @NotBlank(message = "El comentario es obligatorio.")
    private String comentario;

    @NotNull(message = "La calificación del libro es obligartoria.")
    @Min(value = 1, message = "La calificación mínima es 1.")
    @Max(value = 5, message = "La calificación máxima es 5.")
    private Integer calificacion;
}
