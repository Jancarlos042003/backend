package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@Table(name = "detalles_orden")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_orden_paypal")
    private String idOrdenPaypal; // ID de la orden de PayPal

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal descuento;
    private String direccion;
    private String telefono;
}
