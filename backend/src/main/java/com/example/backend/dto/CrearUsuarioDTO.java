package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearUsuarioDTO {
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @Email
    @NotBlank(message = "El email es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;

    private String rol;
}
