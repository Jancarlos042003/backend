package com.example.backend.service;

import com.example.backend.dto.LibroBusquedaDTO;
import com.example.backend.dto.LibroDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class LibroServiceImpl implements LibroService {
    @Autowired
    LibroRepository libroRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    EditorialRepository editorialRepository;

    @Override
    public Libro crearLibro(LibroDTO libroDTO){

        Set<Categoria> categorias = libroDTO.getCategoriasIds().stream()
                .map(id -> categoriaRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException ("Categoría no encontrada con el ID: " + id)))
                .collect(Collectors.toSet());

        Editorial editorial = editorialRepository.findById(libroDTO.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada."));

        Libro libro = Libro.builder()
                .titulo(libroDTO.getTitulo())
                .autor(libroDTO.getAutoresIds())
                .fechaPublicacion(libroDTO.getFechaPublicacion())
                .editorial(editorial)
                .isbn(libroDTO.getIsbn())
                .precio(libroDTO.getPrecio())
                .descuento(libroDTO.getDescuento())
                .descripcion(libroDTO.getDescripcion())
                .resumen(libroDTO.getResumen())
                .vistaPrevia(libroDTO.getVistaPrevia())
                .imgPortada(libroDTO.getImgPortada())
                .imgSubportada(libroDTO.getImgSubportada())
                .categorias(categorias)
                .build();

        return libroRepository.save(libro);
    }

    @Override
    public List<Libro> mostrarLibros(){
        return libroRepository.findAll();
    }

    @Override
    public Libro mostrarLibro(Long id){
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con ID: " + id + " no encontrado."));

        return libro;
    }

    @Override
    public void eliminarLibro(Long id){
        Optional<Libro> libroOptional = libroRepository.findById(id);

        if (libroOptional.isPresent()){
            libroRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Libro no encontrado con el ID: " + id);
        }
    }

    @Override
    public Libro actualizarLibro(Long id, LibroDTO libroActualizado){
        Set<Categoria> categorias = new HashSet<>();
        Editorial editorial = new Editorial();

        Libro libroEncontrado = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con el ID: " + id));

        if (libroActualizado.getCategoriasIds() != null){
            categorias = libroActualizado.getCategoriasIds().stream()
                .map(idCategoria -> categoriaRepository.findById(idCategoria)
                        .orElseThrow(() -> new ResourceNotFoundException ("Categoria no encontrada con el ID: " + idCategoria)))
                .collect(Collectors.toSet());
        }

        if (libroActualizado.getEditorialId() != null){
            editorial = editorialRepository.findById(libroActualizado.getEditorialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada."));
        }

        libroEncontrado.setTitulo(libroActualizado.getTitulo());
        libroEncontrado.setAutor(libroActualizado.getAutoresIds());
        libroEncontrado.setFechaPublicacion(libroActualizado.getFechaPublicacion());
        libroEncontrado.setEditorial(editorial);
        libroEncontrado.setIsbn(libroActualizado.getIsbn());
        libroEncontrado.setPrecio(libroActualizado.getPrecio());
        libroEncontrado.setDescuento(libroActualizado.getDescuento());
        libroEncontrado.setDescripcion(libroActualizado.getDescripcion());
        libroEncontrado.setResumen(libroActualizado.getResumen());
        libroEncontrado.setVistaPrevia(libroActualizado.getVistaPrevia());
        libroEncontrado.setVistaPrevia(libroActualizado.getVistaPrevia());
        libroEncontrado.setImgPortada(libroActualizado.getImgPortada());
        libroEncontrado.setImgSubportada(libroActualizado.getImgSubportada());
        libroEncontrado.setCategorias(categorias);

        // Guardar los cambios
        return libroRepository.save(libroEncontrado);
    }

    @Override
    public List<LibroBusquedaDTO> buscarLibrosPorCriterios(String termino){
        List<Libro> libros = libroRepository.buscarPorCriterios(termino);

        return libros.stream()
                .map(libro -> new LibroBusquedaDTO(libro.getId(), libro.getTitulo(), libro.getIsbn(), libro.getAutor()))
                .collect(Collectors.toList());
    }
}
