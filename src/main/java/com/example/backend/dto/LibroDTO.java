package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private Long id;

    @NotEmpty(message = "El título es obligatorio.")
    private String titulo;

    @NotNull(message = "El autor es obligatorio.")
    private String autor;

    @NotNull(message = "La fecha de publicación es obligatoria.")
    @Past(message = "La fecha de publicación debe ser una fecha en el pasado.")
    private LocalDate fechaPublicacion;

    @NotNull(message = "La editorial es obligatoria.")
    private Long editorialId;

    @NotBlank(message = "El isbn del libro es obligatorio.")
    private String isbn;

    @NotNull(message = "El precio es obligatorio.")
    private BigDecimal precio;

    private BigDecimal descuento;

    @NotBlank(message = "La descripción del libro es obligatoria.")
    private String descripcion;

    @NotBlank(message = "El resumen del libro es obligatorio.")
    private String resumen;

    @NotBlank(message = "La vista previa del libro es obligatoria.")
    private String vistaPrevia;

    @NotBlank(message = "La imagen de portada es obligatoria.")
    private String imgPortada;

    @NotBlank(message = "La subportada del libro es obligatorio.")
    private String imgSubportada;

    @NotNull(message = "Las categorías son obligatorias.")
    @NotEmpty(message = "Debe haber al menos una categoría.")
    private Set<Long> categorias = new HashSet<>();
}
