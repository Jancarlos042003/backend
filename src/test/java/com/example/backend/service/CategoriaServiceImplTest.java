package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.dto.CategoriaDTO;
import com.example.backend.model.Categoria;
import com.example.backend.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Verifica la funcionalidad del Service de Categoria.")
class CategoriaServiceImplTest {
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    @DisplayName("Retorna la categoria creada.")
    void testCrearCategoria(){
        // ARRANGE
        CategoriaDTO categoria = new CategoriaDTO();
        categoria.setNombre("Categoria prueba");

        Categoria categoriaNueva = DataProvider.mostrarCategoriaMocK();

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaNueva);

        // ACT
        Categoria resultado = categoriaService.crearCategoria(categoria);

        // ASSERT
        ArgumentCaptor<Categoria> categoriaCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).save(categoriaCaptor.capture());
        assertEquals("Categoria prueba", categoriaCaptor.getValue().getNombre());

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(1L, resultado.getId()),
                () -> assertEquals("Categoria prueba", resultado.getNombre())
        );

        verify(categoriaRepository,times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Retorna todas las categorias que estan guardadas.")
    void testMostrarCategorias(){
        // ARRANGE
        when(categoriaRepository.findAll()).thenReturn(DataProvider.mostrarCategoriasMocK());

        // ACT
        List<Categoria> resultado = categoriaService.mostrarCategorias();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Elimina una categoria que existe.")
    void testEliminarCategoria(){
        // ARRANGE
        Long id = 1L;
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarCategoriaMocK()));
        doNothing().when(categoriaRepository).deleteById(anyLong());

        // ACT
        categoriaService.eliminarCategoria(id);

        // ASSERT
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(categoriaRepository).findById(argumentCaptor.capture());
        assertEquals(1L, argumentCaptor.getValue());

        verify(categoriaRepository, times(1)).findById(anyLong());
        verify(categoriaRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Retorna error al tratar eliminar una categoria que no existe.")
    void testEliminarCategoria_NoEncontrada(){
        // ARRANGE
        Long id = 5L;
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT
        EntityNotFoundException excepcion = assertThrows(EntityNotFoundException.class, () -> categoriaService.eliminarCategoria(id));

        // ASSERT
        assertAll(
                () -> assertNotNull(excepcion),
                () -> assertEquals("Categoria no encontrada con el ID: " + id, excepcion.getMessage())
        );
        verify(categoriaRepository, times(1)).findById(anyLong());
    }
}
