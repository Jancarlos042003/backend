package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "libro")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;

    @Column(name = "fecha_publicacion")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate fechaPublicacion;

    @ManyToOne
    private Editorial editorial;

    @Column(unique = true)
    private String isbn;

    private BigDecimal precio;
    private BigDecimal descuento;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String resumen;

    @Lob
    @Column(name = "vista_previa", columnDefinition = "TEXT")
    private String vistaPrevia;

    @Column(name = "img_portada")
    private String imgPortada;

    @Column(name = "img_subportada")
    private String imgSubportada;

    @ManyToMany
    @JoinTable(
            name = "libro_categoria",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
}
