package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroBusquedaDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private String autor;
}