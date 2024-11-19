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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class LibroServiceTest {
    @Mock
    private LibroRepository libroRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    @Test
    public void testCrearLibro(){
        // ARRANGE
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Libro prueba");
        libroDTO.setAutoresIds("Autor prueba");
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
        libroDTO.setCategoriasIds(Set.of(1L, 2L));

        when(editorialRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarEditorialMock()));
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarCategoriaMocK()));

        // ACT
        Libro resultado = libroService.crearLibro(libroDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Libro prueba", resultado.getTitulo());
        assertEquals("Autor prueba", resultado.getAutor());
        assertEquals("987654321", resultado.getIsbn());
        assertEquals("Editorial prueba", resultado.getEditorial().getNombre());

        verify(libroRepository, times(1)).save(any(Libro.class));
        verify(categoriaRepository, times(2)).findById(anyLong());
    }

    @Test
    public void testCrearLibro_CategoriaNoEncontrada(){
        // ARRANGE
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setCategoriasIds(Set.of(1L));

        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        assertThrows(ResourceNotFoundException.class, () -> libroService.crearLibro(libroDTO));

        verify(categoriaRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testCrearLibro_EditorialNoEncontrada(){
        // ARRANGE
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setCategoriasIds(Set.of(1L));
        libroDTO.setEditorialId(1L);

        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarCategoriaMocK()));
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        assertThrows(ResourceNotFoundException.class, () -> libroService.crearLibro(libroDTO));

        verify(editorialRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testMostrarLibros(){
        // ARRANGE
        when(libroRepository.findAll()).thenReturn(DataProvider.mostrarLibrosMock());

        // ACT
        List<Libro> resultados = libroService.mostrarLibros();

        // ASSERT
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());

        verify(libroRepository, times(1)).findAll();
    }

    @Test
    public void testMostrarLibro(){
        // ARRANGE
        Long id = 1L;
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));

        // ACT
        Libro resultado = libroService.mostrarLibro(id);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Titulo 1", resultado.getTitulo());
        assertEquals("Editorial 1", resultado.getEditorial().getNombre());
        assertEquals(BigDecimal.valueOf(50), resultado.getPrecio());;
        assertEquals("Resumen", resultado.getResumen());
        assertEquals("Portada", resultado.getImgPortada());

        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testMostrarLibro_NoEncontrado(){
        // ARRANGE
        when(libroRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            libroService.mostrarLibro(anyLong());
        });

        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testEliminarLibro(){
        // ARRANGE
        Long id = 1L;
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        doNothing().when(libroRepository).deleteById(anyLong());

        // ACT
        libroService.eliminarLibro(id);

        // ASSERT
        verify(libroRepository, times(1)).findById(anyLong());
        verify(libroRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testEliminarLibro_NoEncontrado(){
        // ARRANGE
        when(libroRepository.findById(anyLong())).thenReturn(Optional.empty()); // Indica la ausencia de un valor en lugar de devolver null

        // ACT-ASSERT
        assertThrows(EntityNotFoundException.class, () -> libroService.eliminarLibro(anyLong()));

        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testActualizarLibro(){
        // ARRANGE
        Long id = 1L;

        Categoria categoria3 = new Categoria(3L, "Categoría 3");
        Categoria categoria4 = new Categoria(4L, "Categoría 4");
        Set<Categoria> categorias = new HashSet<>() {{ add(categoria3); add(categoria4); }};

        Editorial editorial = new Editorial(2L, "Editorial actualizada");

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Titulo actualizado");
        libroDTO.setAutoresIds("Autor actualizado");
        libroDTO.setFechaPublicacion(LocalDate.of(2010, 10, 10));
        libroDTO.setEditorialId(2L);
        libroDTO.setIsbn("987654321");
        libroDTO.setPrecio(BigDecimal.valueOf(24.99));
        libroDTO.setDescuento(BigDecimal.valueOf(15.0));
        libroDTO.setDescripcion("Descripción actualizada");
        libroDTO.setResumen("Resumen actualizado");
        libroDTO.setVistaPrevia("www.ejemplo.com/vistaPreviewActualizada");
        libroDTO.setImgPortada("www.ejemplo.com/imgPortadaActualizada");
        libroDTO.setImgSubportada("www.ejemplo.com/imgSubportadaActualizada");
        libroDTO.setCategoriasIds(new HashSet<>() {{ add(3L); add(4L); }});

        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.of(editorial));
        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(categoria3));
        when(categoriaRepository.findById(4L)).thenReturn(Optional.of(categoria4));


        // ACT
        Libro resultado = libroService.actualizarLibro(id, libroDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Titulo actualizado", resultado.getTitulo());
        assertEquals(LocalDate.of(2010, 10, 10),resultado.getFechaPublicacion());
        assertEquals("Autor actualizado", resultado.getAutor());
        assertEquals(LocalDate.of(2010, 10, 10), resultado.getFechaPublicacion());
        assertEquals(editorial, resultado.getEditorial());
        assertEquals("987654321", resultado.getIsbn());
        assertEquals(BigDecimal.valueOf(24.99), resultado.getPrecio());
        assertEquals(BigDecimal.valueOf(15.0), resultado.getDescuento());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("Resumen actualizado", resultado.getResumen());
        assertEquals("www.ejemplo.com/vistaPreviewActualizada", resultado.getVistaPrevia());
        assertEquals("www.ejemplo.com/imgPortadaActualizada", resultado.getImgPortada());
        assertEquals("www.ejemplo.com/imgSubportadaActualizada", resultado.getImgSubportada());
        assertEquals(categorias, resultado.getCategorias());

        verify(libroRepository, times(1)).findById(anyLong());
        verify(libroRepository, times(1)).save(any(Libro.class)); // Asegurarnos que se guarde en la BD
    }

    @Test
    public void testActualizarLibro_libroNoEncontrado(){
        // ARRANGE
        when(libroRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        assertThrows(ResourceNotFoundException.class, () -> libroService.actualizarLibro(anyLong(), new LibroDTO()));

        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testActualizarLibro_editorialNoEncontrada(){
        // ARRANGE
        Long id = 1L;

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setEditorialId(1L);

        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        when(editorialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        assertThrows(ResourceNotFoundException.class, () -> libroService.actualizarLibro(id, libroDTO));

        verify(libroRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testActualizarLibro_categoriaNoEncontrada(){
        // ARRANGE
        Long id = 1L;
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setCategoriasIds(Set.of(1L));

        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(DataProvider.mostrarLibroMocK()));
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT-ASSERT
        assertThrows(ResourceNotFoundException.class, () -> libroService.actualizarLibro(id, libroDTO));

        verify(libroRepository, times(1)).findById(anyLong());
        verify(categoriaRepository, times(1)).findById(anyLong());
    }
}
