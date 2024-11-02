package com.example.backend.controller;

import com.example.backend.dto.RespuestaPagoDTO;
import com.example.backend.dto.SolicitudOrdenDTO;
import com.example.backend.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PaypalController {

    private final PayPalService paypalService;

    @PostMapping("/crear")
    public ResponseEntity<RespuestaPagoDTO> crearOrden(@RequestBody SolicitudOrdenDTO solicitudOrden) {
        return ResponseEntity.ok(paypalService.crearOrden(solicitudOrden));
    }

    @PostMapping("/capturar/{ordenId}")
    public ResponseEntity<RespuestaPagoDTO> capturarOrden(@PathVariable String ordenId) {
        return ResponseEntity.ok(paypalService.capturarOrden(ordenId));
    }
}
