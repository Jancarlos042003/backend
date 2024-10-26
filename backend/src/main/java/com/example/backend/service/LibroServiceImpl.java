package com.example.backend.service;

import com.example.backend.dto.LibroDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    SubcategoriaRepository subcategoriaRepository;

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
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con el ID: " + id));


        Set<Categoria> categorias = libroActualizado.getCategoriasIds().stream()
                .map(idCategoria -> categoriaRepository.findById(idCategoria)
                        .orElseThrow(() -> new ResourceNotFoundException ("Categoria no encontrada con el ID: " + idCategoria)))
                .collect(Collectors.toSet());

        Editorial editorial = editorialRepository.findById(libroActualizado.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada."));

        if(libroActualizado.getTitulo() != null){
            libro.setTitulo(libroActualizado.getTitulo());
        }
        if(libroActualizado.getAutoresIds() != null){
            libro.setAutor(libroActualizado.getAutoresIds());
        }
        if(libroActualizado.getFechaPublicacion() != null){
            libro.setFechaPublicacion(libroActualizado.getFechaPublicacion());
        }
        if(libroActualizado.getEditorialId() != null){
            libro.setEditorial(editorial);
        }
        if(libroActualizado.getIsbn() != null){
            libro.setIsbn(libroActualizado.getIsbn());
        }
        if(libroActualizado.getPrecio() != null){
            libro.setPrecio(libroActualizado.getPrecio());
        }
        if(libroActualizado.getDescuento() != null){
            libro.setDescuento(libroActualizado.getDescuento());
        }
        if(libroActualizado.getDescripcion() != null){
            libro.setDescripcion(libroActualizado.getDescripcion());
        }
        if(libroActualizado.getResumen() != null){
            libro.setResumen(libroActualizado.getResumen());
        }
        if(libroActualizado.getVistaPrevia() != null){
            libro.setVistaPrevia(libroActualizado.getVistaPrevia());
        }
        if(libroActualizado.getImgPortada() != null){
            libro.setImgPortada(libroActualizado.getImgPortada());
        }
        if(libroActualizado.getImgSubportada() != null){
            libro.setImgSubportada(libroActualizado.getImgSubportada());
        }
        if(libroActualizado.getCategoriasIds() != null){
            libro.setCategorias(categorias);
        }

        // Guardar los cambios
        return libroRepository.save(libro);
    }
}
