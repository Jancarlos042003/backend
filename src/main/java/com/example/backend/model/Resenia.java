package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resenia")
public class Resenia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_libro")
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String comentario;

    private Integer calificacion;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;
}
