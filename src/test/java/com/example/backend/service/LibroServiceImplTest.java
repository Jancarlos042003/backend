package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.dto.LibroDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Categoria;
import com.example.backend.model.Editorial;
import com.example.backend.model.Libro;
import com.example.backend.repository.CategoriaRepository;
import com.example.backend.repository.EditorialRepository;
import com.example.backend.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Verificar el funcionamiento del service de Libro")
class LibroServiceImplTest {
    @Mock
    private LibroRepository libroRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    @Test
    @DisplayName("Retorna el nuevo libro creado")
    void testCrearLibro(){
        // ARRANGE
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Título prueba");
        libroDTO.setAutor("Autor prueba");
        libroDTO.setFechaPublicacion(LocalDate.of(1976, 5, 30));
        libroDTO.setEditorialId(1L);
        libroDTO.setIsbn("987654321");
        libroDTO.setPrecio(BigDecimal.valueOf(50));
        libroDTO.setDescuento(BigDecimal.valueOf(15));
        libroDTO.setDescripcion("Descripcion del libro.");
        libroDTO.setResumen("Resumen del libro.");
        libroDTO.setVistaPrevia("Vista previa del libro");
        libroDTO.setImgPortada("URL imagen portada");
        libroDTO.setImgSubportada("URL imagen subportada");
        libroDTO.setCategorias(Set.of(1L, 2L));

        Libro libroNuevo = DataProvider.mostrarLibroMocK();
        Categoria categoria1 = libroNuevo.getCategorias().stream()
                        .filter(cat -> cat.getId().equals(1L)).findFirst().orElse(null);
        Categoria categoria2 = libroNuevo.getCategorias().stream()
                .filter(cat -> cat.getId().equals(2L)).findFirst().orElse(null);

        when(editorialRepository.findById(anyLong())).thenReturn(Optional.of(libroNuevo.getEditorial()));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria1));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria2));
        when(libroRepository.save(any(Libro.class))).thenReturn(libroNuevo);

        // ACT
        Libro resultado = libroService.crearLibro(libroDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Título prueba", resultado.getTitulo());
        assertEquals("Autor prueba", resultado.getAutor());
        assertEquals("987654321", resultado.getIsbn());
        assertEquals(BigDecimal.valueOf(50), resultado.getPrecio());
        assertEquals("Editorial prueba", resultado.getEditorial().getNombre());
        assertEquals(2, resultado.getCategorias().size());

        verify(libroRepository, times(1)).save(any(Libro.class));
        verify(categoriaRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna error al tratar de encontrar una categoria que no exite")
    void testCrearLibro_CategoriaNoEncontrada(){
        // ARRANGE
        Long id = 7L;
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setCategorias(Set.of(id));

        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> libroService.crearLibro(libroDTO));

        // ASSERT
        assertAll(
                () -> assertEquals("Categoría no encontrada con el ID: " + id, exception.getMessage())
        );
        verify(categoriaRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna error al tratar de encontrar un editorial que no exite")
    void testCrearLibro_EditorialNoEncontrada(){
        // ARRANGE
        Long idEditorial = 4L;
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setCategorias(Set.of(1L));
        libroDTO.setEditorialId(idEditorial);

        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarCategoriaMocK()));
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> libroService.crearLibro(libroDTO));

        // ASSERT
        assertAll(
                () -> assertEquals("Editorial no encontrada.", exception.getMessage())
        );
        verify(editorialRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna una lista de todos los libros")
    void testMostrarLibros(){
        // ARRANGE
        when(libroRepository.findAll()).thenReturn(DataProvider.mostrarLibrosMock());

        // ACT
        List<Libro> resultados = libroService.mostrarLibros();

        // ASSERT
        assertAll(
                () -> assertNotNull(resultados),
                () -> assertFalse(resultados.isEmpty())
        );

        verify(libroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retorna un libro buscado por su ID")
    void testMostrarLibro(){
        // ARRANGE
        Long id = 1L;
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));

        // ACT
        Libro resultado = libroService.mostrarLibro(id);

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals("Título prueba", resultado.getTitulo()),
                () -> assertEquals("Editorial prueba", resultado.getEditorial().getNombre()),
                () -> assertEquals(BigDecimal.valueOf(50), resultado.getPrecio()),
                () -> assertEquals(2, resultado.getCategorias().size()),
                () -> assertEquals("Resumen", resultado.getResumen()),
                () -> assertEquals("Portada", resultado.getImgPortada())
        );

        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna error al no encontrar un libro por su ID")
    void testMostrarLibro_NoEncontrado(){
        // ARRANGE
        Long id = 6L;
        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            libroService.mostrarLibro(id);
        });

        // ASSERT
        assertAll(
                () -> assertEquals("Libro con ID: " + id + " no encontrado.", exception.getMessage())
        );
        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Eliminar un libro por su ID")
    void testEliminarLibro(){
        // ARRANGE
        Long id = 1L;
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        doNothing().when(libroRepository).deleteById(anyLong());

        // ACT
        libroService.eliminarLibro(id);

        // ASSERT
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(libroRepository).deleteById(argumentCaptor.capture());
        assertEquals(1L, argumentCaptor.getValue());

        verify(libroRepository, times(1)).findById(anyLong());
        verify(libroRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Error al eliminar un libro. ID del libro no existe")
    public void testEliminarLibro_NoEncontrado(){
        // ARRANGE
        Long id = 6L;
        when(libroRepository.findById(id)).thenReturn(Optional.empty()); // Indica la ausencia de un valor en lugar de devolver null

        // ACT
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> libroService.eliminarLibro(id));

        // ASSERT
        assertAll(
                () -> assertEquals("Libro no encontrado con el ID: " + id, exception.getMessage())
        );
        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna el libro actualizado")
    void testActualizarLibro(){
        // ARRANGE
        Long id = 1L;

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Titulo actualizado");
        libroDTO.setAutor("Autor actualizado");
        libroDTO.setFechaPublicacion(LocalDate.of(2010, 10, 10));
        libroDTO.setEditorialId(2L);
        libroDTO.setIsbn("987654321");
        libroDTO.setPrecio(BigDecimal.valueOf(24.99));
        libroDTO.setDescuento(BigDecimal.valueOf(15.0));
        libroDTO.setDescripcion("Descripción actualizada");
        libroDTO.setResumen("Resumen actualizado");
        libroDTO.setVistaPrevia("Preview Actualizada");
        libroDTO.setImgPortada("Portada Actualizada");
        libroDTO.setImgSubportada("Subportada Actualizada");
        libroDTO.setCategorias(Set.of(3L, 4L));

        // Se crean las 2 categorias nuevas
        Categoria categoria1 = new Categoria(3L, "Categoria 3");
        Categoria categoria2 = new Categoria(4L, "Categoria 4");

        Libro libroEncontrado = DataProvider.mostrarLibroMocK();

        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(libroEncontrado));
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.of(libroEncontrado.getEditorial()));

        // No se puede utilizar el mismo mock para ambos IDs ya que 'Set' elimina los duplicados.
        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(categoria1));
        when(categoriaRepository.findById(4L)).thenReturn(Optional.of(categoria2));
        when(libroRepository.save(any(Libro.class))).thenAnswer(e -> e.getArgument(0));

        // ACT
        Libro resultado = libroService.actualizarLibro(id, libroDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Titulo actualizado", resultado.getTitulo());
        assertEquals("Autor actualizado", resultado.getAutor());
        assertEquals(LocalDate.of(2010, 10, 10), resultado.getFechaPublicacion());
        assertEquals("Editorial prueba", resultado.getEditorial().getNombre());
        assertEquals("987654321", resultado.getIsbn());
        assertEquals(BigDecimal.valueOf(24.99), resultado.getPrecio());
        assertEquals(BigDecimal.valueOf(15.0), resultado.getDescuento());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("Resumen actualizado", resultado.getResumen());
        assertEquals("Preview Actualizada", resultado.getVistaPrevia());
        assertEquals("Portada Actualizada", resultado.getImgPortada());
        assertEquals("Subportada Actualizada", resultado.getImgSubportada());
        assertEquals(2, resultado.getCategorias().size());

        verify(libroRepository, times(1)).findById(anyLong());
        verify(libroRepository, times(1)).save(any(Libro.class)); // Asegurarnos que se guarde en la BD
        verify(categoriaRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al actualizar un libro. ID del libro no existente")
    void testActualizarLibro_libroNoEncontrado(){
        // ARRANGE
        Long id = 6L;
        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> libroService.actualizarLibro(id, new LibroDTO()));

        // ASSERT
        assertAll(
                () -> assertEquals("Libro no encontrado con el ID: " + id, exception.getMessage())
        );
        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al actualizar un libro. ID de la editorial no existente")
    void testActualizarLibro_editorialNoEncontrada(){
        // ARRANGE
        Long idLibro = 1L;
        Long idEditorial = 6L;

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setEditorialId(idEditorial);

        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        when(editorialRepository.findById(idEditorial)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> libroService.actualizarLibro(idLibro, libroDTO));

        // ASSERT
        assertAll(
                () -> assertEquals("Editorial no encontrada.", exception.getMessage())
        );
        verify(libroRepository, times(1)).findById(anyLong());
        verify(editorialRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al actualizar un libro. ID de la categoria no existente")
    void testActualizarLibro_categoriaNoEncontrada(){
        // ARRANGE
        Long idLibro = 1L;
        Long idCategoria  = 6L;
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setCategorias(Set.of(idCategoria));

        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> libroService.actualizarLibro(idLibro, libroDTO));

        // ASSERT
        assertAll(
                () -> assertEquals("Categoria no encontrada con el ID: " + idCategoria, exception.getMessage())
        );
        verify(libroRepository, times(1)).findById(anyLong());
        verify(categoriaRepository, times(1)).findById(anyLong());
    }
}
