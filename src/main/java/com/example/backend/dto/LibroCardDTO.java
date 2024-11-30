package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibroCardDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private BigDecimal precio;
    private BigDecimal descuento;
    private String descripcion;
    private String imgPortada;
}
