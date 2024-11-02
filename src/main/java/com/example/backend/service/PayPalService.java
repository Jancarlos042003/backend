package com.example.backend.service;

import com.example.backend.dto.RespuestaPagoDTO;
import com.example.backend.dto.SolicitudOrdenDTO;

public interface PayPalService {
    RespuestaPagoDTO crearOrden(SolicitudOrdenDTO solicitudOrden);

    RespuestaPagoDTO capturarOrden(String ordenId);
}
