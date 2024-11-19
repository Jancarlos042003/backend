package com.example.backend;

import com.example.backend.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataProvider {

    public static List<Rol> listaRolesMock(){
        return List.of(
                new Rol(1L, "USER"),
                new Rol(2L, "ADMIN")
        );
    }

    public static Usuario usuarioMock(){
        return new Usuario(2L , "Usuario prueba", "usuario@gmail.com", "encodedPassword", listaRolesMock().get(0), LocalDate.of(2024, 1, 1));
    }

    public static List<Usuario> listaUsuariosMock(){
        return List.of(
                new Usuario(1L , "Juan Peréz", "juan@gmail.com", "juan", listaRolesMock().get(0), LocalDate.of(2024, 5, 11)),
                new Usuario(2L , "Cristiano Ronaldo", "cristiano@gmail.com", "cristiano", listaRolesMock().get(1), LocalDate.of(2024, 1, 1))
        );
    }

    public static Editorial mostrarEditorialMock(){
        return new Editorial(1L, "Editorial prueba");
    }

    public static List<Editorial> mostrarEditorialesMock(){
        return List.of(
                new Editorial(1L, "Editorial prueba 1"),
                new Editorial(2L, "Editorial prueba 2")
        );
    }

    public static List<Libro> mostrarLibrosMock(){
        Set<Categoria> categorias = new HashSet<>();
        categorias.add(new Categoria(1L, "Categoria 1"));
        categorias.add(new Categoria(2L, "Categoria 2"));

        Editorial editorial = mostrarEditorialMock();

        return List.of(
                new Libro(1L, "Titulo 1", "Autor 1", LocalDate.now(), editorial, "987654321", BigDecimal.valueOf(50), BigDecimal.valueOf(15), "Descripcion", "Resumen", "Vista Previa", "Portada", "Subportada", categorias),
                new Libro(2L, "Titulo 2", "Autor 2", LocalDate.now(), editorial, "123456789", BigDecimal.valueOf(40), BigDecimal.valueOf(10), "Descripcion", "Resumen", "Vista Previa", "Portada", "Subportada", categorias)
        );
    }

    public static Libro mostrarLibroMocK(){
        Set<Categoria> categorias = new HashSet<>();
        categorias.add(new Categoria(1L, "Categoria 1"));
        categorias.add(new Categoria(2L, "Categoria 2"));

        Editorial editorial = mostrarEditorialMock();

        return new Libro(1L, "Título prueba", "Autor prueba", LocalDate.of(2000, 10, 1), editorial, "987654321", BigDecimal.valueOf(50), BigDecimal.valueOf(15), "Descripcion", "Resumen", "Vista Previa", "Portada", "Subportada", categorias);

    }

    public static Categoria mostrarCategoriaMocK(){
        return new Categoria(1L, "Categoria prueba");
    }

    public static List<Categoria> mostrarCategoriasMocK(){
        return List.of(
                new Categoria(1L, "Ficcion"),
                new Categoria(2L, "Terror")
        );
    }

    public static Resenia mostrarReseniaMock(){
        Usuario usuario = usuarioMock();
        Libro libro = mostrarLibroMocK();

        return new Resenia(1L, libro, usuario, "Comentario prueba", 5, LocalDate.now());
    }

    public static Set<Resenia> mostrarReseniasMock(){
        Usuario usuario = usuarioMock();
        Libro libro = mostrarLibroMocK();

        Set<Resenia> resenias = new HashSet<>();

        resenias.add(new Resenia(1L, libro, usuario, "Comentario 1", 5, LocalDate.now()));
        resenias.add(new Resenia(2L, libro, usuario, "Comentario 2", 3, LocalDate.now()));

        return  resenias;
    }
}
