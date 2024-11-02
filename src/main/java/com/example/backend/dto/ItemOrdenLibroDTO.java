package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemOrdenLibroDTO {
    @NotNull(message = "El ID del libro no puede ser nulo.")
    private Long idLibro;

    @Positive(message = "La unidad de libros debe ser mayor a 0.")
    private int cantidad;

    @NotNull(message = "El precio es obligatorio.")
    private BigDecimal precio;

    private BigDecimal descuento;
}
