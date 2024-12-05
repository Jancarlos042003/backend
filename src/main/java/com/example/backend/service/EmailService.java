package com.example.backend.service;

import com.example.backend.model.Orden;

public interface EmailService {
    void enviarBoletaCompra(Orden orden, String emailDestino);
}
