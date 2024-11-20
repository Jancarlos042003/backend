package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InformacionEnvioDTO {
    @NotBlank(message = "Se necesita ingresar una dirección de envío.")
    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccion;

    @NotBlank(message = "Es obligatorio ingresar un número de teléfono.")
    @Size(max = 20, message = "El número de teléfono no puede exceder los 20 caracteres.")
    private String telefono;
}