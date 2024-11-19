package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.dto.ReseniaDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Resenia;
import com.example.backend.repository.LibroRepository;
import com.example.backend.repository.ReseniaRepository;
import com.example.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReseniaServiceImpTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private ReseniaRepository reseniaRepository;

    @InjectMocks
    private ReseniaServiceImpl reseniaService;

    @Test
    @DisplayName("Retorna la nueva reseña creada")
    void testCrearResenia(){
        // ARRANGE
        Long idLibro = 1L;
        Long idUsuario = 1L;
        ReseniaDTO reseniaDTO = new ReseniaDTO();
        reseniaDTO.setComentario("Comentario prueba");
        reseniaDTO.setCalificacion(5);

        Resenia reseniaNueva = DataProvider.mostrarReseniaMock();

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(reseniaNueva.getUsuario()));
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(reseniaNueva.getLibro()));
        when(reseniaRepository.save(any(Resenia.class))).thenReturn(reseniaNueva);

        // ACT
        Resenia resultado = reseniaService.crearResenia(reseniaDTO, idLibro, idUsuario);

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals("Usuario prueba", resultado.getUsuario().getNombreCompleto()),
                () -> assertEquals("Título prueba", resultado.getLibro().getTitulo()),
                () -> assertEquals("Comentario prueba", resultado.getComentario()),
                () -> assertEquals(5, resultado.getCalificacion())
        );

        // El ArgumentCaptor te permite verificar que los datos del DTO se transformaron correctamente en la entidad Resenia antes de ser guardados.
        ArgumentCaptor<Resenia> reseniaCaptor = ArgumentCaptor.forClass(Resenia.class);
        verify(reseniaRepository).save(reseniaCaptor.capture());

        // Verificar contenido de la reseña guardada
        Resenia reseniaGuardada = reseniaCaptor.getValue();
        assertEquals("Comentario prueba", reseniaGuardada.getComentario());
        assertEquals(5, reseniaGuardada.getCalificacion());

        verify(reseniaRepository,times(1)).save(any(Resenia.class));
        verify(usuarioRepository, times(1)).findById(anyLong());
        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al crear la reseña. Usuario no encontrado por su ID")
    void testCrearResenia_UsuarioNoEncontrado(){
        // ARRANGE
        Long idLibro = 1L;
        Long idUsuario = 1L;
        ReseniaDTO reseniaDTO = new ReseniaDTO();

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reseniaService.crearResenia(reseniaDTO, idLibro, idUsuario));

        // ASSERT
        assertAll(
                () -> assertEquals("Usuario no encontrado con el ID: " + idUsuario, exception.getMessage())
        );
        verify(usuarioRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al crear la reseña. Libro no encontrado por su ID")
    void testCrearResenia_LibroNoEncontrado(){
        // ARRANGE
        Long idLibro = 1L;
        Long idUsuario = 1L;
        ReseniaDTO reseniaDTO = new ReseniaDTO();

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(DataProvider.usuarioMock()));
        when(libroRepository.findById(idLibro)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reseniaService.crearResenia(reseniaDTO, idLibro, idUsuario));

        // ASSERT
        assertAll(
                () -> assertEquals("Libro no encontrado con el ID: " + idLibro, exception.getMessage())
        );
        verify(usuarioRepository, times(1)).findById(anyLong());
        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna la reseña editada")
    void testEditarResenia(){
        // ARRANGE
        Long idResenia = 1L;
        ReseniaDTO reseniaDTO = new ReseniaDTO();
        reseniaDTO.setComentario("Comentario prueba editado");
        reseniaDTO.setCalificacion(3);

        Resenia reseniaExistente = DataProvider.mostrarReseniaMock();

        when(reseniaRepository.findById(idResenia)).thenReturn(Optional.of(reseniaExistente));
        when(reseniaRepository.save(any(Resenia.class))).thenAnswer(e -> e.getArgument(0));

        // ACT
        Resenia resultado = reseniaService.editarResenia(reseniaDTO, idResenia);

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals("Comentario prueba editado", resultado.getComentario()),
                () -> assertEquals(3, resultado.getCalificacion())
        );

        verify(reseniaRepository, times(1)).findById(anyLong());
        verify(reseniaRepository, times(1)).save(any(Resenia.class));
    }

    @Test
    @DisplayName("Error al editar la reseña. Reseña no encontrada por su ID")
    void testEditarResenia_ReseniaNoEncontrada(){
        // ARRANGE
        Long idResenia = 6L;
        ReseniaDTO reseniaDTO = new ReseniaDTO();

        when(reseniaRepository.findById(idResenia)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reseniaService.editarResenia(reseniaDTO, idResenia));

        //ASSERT
        assertAll(
                () -> assertEquals("Resenia no encontrada con el ID: " + idResenia, exception.getMessage())
        );
        verify(reseniaRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Elimina una reseña por su ID")
    void testEliminarResenia(){
        // ARRANGE
        Long idResenia = 1L;

        when(reseniaRepository.findById(idResenia)).thenReturn(Optional.of(DataProvider.mostrarReseniaMock()));
        doNothing().when(reseniaRepository).deleteById(anyLong());

        // ACT
        reseniaService.eliminarResenia(idResenia);

        // ASSERT
        ArgumentCaptor<Long> reseniaCaptor = ArgumentCaptor.forClass(Long.class);
        verify(reseniaRepository).deleteById(reseniaCaptor.capture());

        assertEquals(1L, reseniaCaptor.getValue());

        verify(reseniaRepository, times(1)).findById(anyLong());
        verify(reseniaRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Error al eliminar una reseña. Reseña no encontrada por su ID")
    void testEliminarResenia_ReseniaNoEncontrada(){
        // ARRANGE
        Long idResenia = 6L;

        when(reseniaRepository.findById(idResenia)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reseniaService.eliminarResenia(idResenia));

        // ASSERT
        assertAll(
                () -> assertEquals("Resenia no encontrada con el ID: " + idResenia, exception.getMessage())
        );
        verify(reseniaRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna las reseñas que tiene el libro")
    void testMostrarReseniasPorLibro(){
        // ARRANGE
        Long id = 1L;

        when(libroRepository.findById(id)).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        when(reseniaRepository.findAllByLibroId(anyLong())).thenReturn(DataProvider.mostrarReseniasMock());

        // ACT
        Set<Resenia> resultado = reseniaService.mostrarReseniaPorLibro(id);

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(reseniaRepository, times(1)).findAllByLibroId(anyLong());
        verify(libroRepository,times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al mostrar las reseñas del libro. Libro no encontrado por su ID")
    void testMostrarReseniasPorLibro_LibroNoEncontrado(){
        // ARRANGE
        Long idLibro = 6L;

        when(libroRepository.findById(idLibro)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reseniaService.mostrarReseniaPorLibro(idLibro));

        // ASSERT
        assertAll(
                ()-> assertEquals("Libro no encontrado con el ID: " + idLibro, exception.getMessage())
        );
        verify(libroRepository,times(1)).findById(anyLong());
    }
}
