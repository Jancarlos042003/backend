package com.example.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SolicitudOrdenDTO {
    @NotNull(message = "El ID del usuario no puede ser nulo.")
    private Long idUsuario;

    @NotEmpty(message = "Debe de tener al menos un libro.")
    private List<ItemOrdenLibroDTO> items;

    @Positive(message = "El monto total debe ser mayor a 0.")
    private BigDecimal total;

    @NotNull(message = "La información de envío es obligatoria.")
    private InformacionEnvioDTO informacionEnvio;
}