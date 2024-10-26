package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
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

    /*@ManyToMany
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )*/
    private String autor;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "editorial_id")
    private Editorial editorial;

    @Column(unique = true)
    private String isbn;

    private BigDecimal precio;
    private BigDecimal descuento;
    private String descripcion;
    private String resumen;

    @Column(name = "vista_previa")
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
    private Set<Categoria> categorias;

    /*@ManyToMany
    @JoinTable(
            name = "libro_subcategoria",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategoria_id")
    )
    private Set<Subcategoria> subcategorias;*/
}
