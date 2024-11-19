package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.dto.EditorialDTO;
import com.example.backend.model.Editorial;
import com.example.backend.repository.EditorialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Verifica la funcionalidad del Service de Editorial.")
public class EditorialServiceImplTest {
    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private EditorialServiceImpl editorialService;

    @Test
    @DisplayName("Retorna el nuevo editorial creado")
    void testCrearEditorial(){
        // ARRANGE
        EditorialDTO editorialDTO = new EditorialDTO();
        editorialDTO.setNombre("Editorial prueba");

        when(editorialRepository.save(any(Editorial.class))).thenReturn(DataProvider.mostrarEditorialMock());

        // ACT
        Editorial resultado = editorialService.crearEditorial(editorialDTO);

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(1L, resultado.getId()),
                () -> assertEquals("Editorial prueba", resultado.getNombre())
        );
        verify(editorialRepository, times(1)).save(any(Editorial.class));
    }

    @Test
    @DisplayName("Retorna una lista de todos los editoriales")
    void testMostrarEditoriales(){
        // ARRANGE
        when(editorialRepository.findAll()).thenReturn(DataProvider.mostrarEditorialesMock());

        // ACT
        List<Editorial> resultado = editorialService.mostrarEditoriales();

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals("Editorial prueba 1", resultado.get(0).getNombre()),
                () -> assertEquals(2L, resultado.get(1).getId())
        );
        verify(editorialRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Elimina un editorial que existe")
    void testEliminarEditorial(){
        // ARRANGE
        Long id = 1L;
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarEditorialMock()));
        doNothing().when(editorialRepository).deleteById(anyLong());

        // ACT
        editorialService.eliminarEditorial(id);

        // ASSERT
        verify(editorialRepository, times(1)).findById(anyLong());
        verify(editorialRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Retorna error al tratar de eliminar un editorial que no existe")
    void testEliminarEditorial_NoEncontrado(){
        // ARRANGE
        Long id = 1L;
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> editorialService.eliminarEditorial(id));
        assertAll(
                () -> assertEquals("Editorial no encontrado con el ID: "+ id, exception.getMessage())
        );
        verify(editorialRepository, times(1)).findById(anyLong());
    }
}
