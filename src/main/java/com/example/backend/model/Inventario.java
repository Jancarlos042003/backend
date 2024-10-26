package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    private Integer cantidad;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;
}
