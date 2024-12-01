package com.example.backend.service;

import com.example.backend.dto.LibroBusquedaDTO;
import com.example.backend.dto.LibroCardDTO;
import com.example.backend.dto.LibroDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    ModelMapper modelMapper;

    @Override
    public Libro crearLibro(LibroDTO libroDTO){

        Set<Categoria> categorias = libroDTO.getCategorias().stream()
                .map(id -> categoriaRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException ("Categoría no encontrada con el ID: " + id)))
                .collect(Collectors.toSet());

        Editorial editorial = editorialRepository.findById(libroDTO.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada."));

        Libro libro = Libro.builder()
                .titulo(libroDTO.getTitulo())
                .autor(libroDTO.getAutor())
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
    public List<LibroCardDTO> mostrarLibros(){
        List<Libro> libros = libroRepository.findAll();
        return libros.stream()
                .map(libro -> LibroCardDTO.builder()
                        .id(libro.getId())
                        .titulo(libro.getTitulo())
                        .autor(libro.getAutor())
                        .isbn(libro.getIsbn())
                        .precio(libro.getPrecio())
                        .descuento(libro.getDescuento())
                        .descripcion(libro.getDescripcion())
                        .imgPortada(libro.getImgPortada())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Libro mostrarLibro(Long id){
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con ID: " + id + " no encontrado."));

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

        if (libroActualizado.getCategorias() != null){
            categorias = libroActualizado.getCategorias().stream()
                .map(idCategoria -> categoriaRepository.findById(idCategoria)
                        .orElseThrow(() -> new ResourceNotFoundException ("Categoria no encontrada con el ID: " + idCategoria)))
                .collect(Collectors.toSet());
        }

        if (libroActualizado.getEditorialId() != null){
            editorial = editorialRepository.findById(libroActualizado.getEditorialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada."));
        }

        libroEncontrado.setTitulo(libroActualizado.getTitulo());
        libroEncontrado.setAutor(libroActualizado.getAutor());
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

    @Override
    public List<LibroDTO> mostrarLibrosPorCategoria(Long id){
        List<Libro> libroList = libroRepository.findByCategoriasId(id).orElse(Collections.emptyList());

        return libroList.stream()
                .map(libro -> modelMapper.map(libro, LibroDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroCardDTO> obtenerLibrosPorCategoria(Long categoriaId) {
        return libroRepository.findLibroCardsByCategoriaId(categoriaId);
    }

}
