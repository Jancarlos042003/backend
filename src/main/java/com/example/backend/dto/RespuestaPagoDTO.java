package com.example.backend.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class RespuestaPagoDTO {
    private String idOrden;
    private String estado;
    private String urlAprobacion;
}