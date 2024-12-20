package com.example.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {
    private Long id;
    private LibroCardDTO libroCardDTO;

    @NotNull(message = "La calificación del libro es obligatoria.")
    @Min(value = 1, message = "El stock debe ser mayor a 0.")
    private Integer stock;

    private Integer entrada;
    private Integer salida;
    private boolean agotado;
    private String numLote;
    private LocalDate fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
