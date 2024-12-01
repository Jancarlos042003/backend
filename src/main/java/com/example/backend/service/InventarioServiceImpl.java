package com.example.backend.service;

import com.example.backend.dto.InventarioDTO;
import com.example.backend.dto.LibroCardDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Inventario;
import com.example.backend.model.Libro;
import com.example.backend.repository.InventarioRepository;
import com.example.backend.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements InventarioService {
    @Autowired
    InventarioRepository inventarioRepository;

    @Autowired
    LibroRepository libroRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<InventarioDTO> mostrarInventarios(){
        List<Inventario> inventarioList = inventarioRepository.findAll();

        return inventarioList.stream()
                .map(inventario -> InventarioDTO.builder()
                        .id(inventario.getId())
                        .stock(inventario.getStock())
                        .entrada(inventario.getEntrada())
                        .salida(inventario.getSalida())
                        .agotado(inventario.isAgotado())
                        .numLote(inventario.getNumLote())
                        .fechaCreacion(inventario.getFechaCreacion())
                        .fechaActualizacion(inventario.getFechaActualizacion())
                        .libroCardDTO(LibroCardDTO.builder()
                                .id(inventario.getLibro().getId())
                                .titulo(inventario.getLibro().getTitulo())
                                .autor(inventario.getLibro().getAutor())
                                .isbn(inventario.getLibro().getIsbn())
                                .precio(inventario.getLibro().getPrecio())
                                .descuento(inventario.getLibro().getDescuento())
                                .descripcion(inventario.getLibro().getDescripcion())
                                .imgPortada(inventario.getLibro().getImgPortada())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioDTO> buscarInventarioPorCriterios(String termino){
        List<Inventario> inventarios = inventarioRepository.buscarPorCriterios(termino);
        return inventarios.stream()
                .map(inventario -> modelMapper.map(inventario, InventarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public InventarioDTO crearInventario(InventarioDTO inventarioDTO){
        Libro libro = libroRepository.findById(inventarioDTO.getLibroCardDTO().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        Inventario inventario = Inventario.builder()
                .libro(libro)
                .stock(inventarioDTO.getStock())
                .fechaCreacion(LocalDate.now())
                .fechaActualizacion(LocalDateTime.now())
                .entrada(inventarioDTO.getEntrada())
                .salida(0)
                .agotado(false)
                .numLote(inventarioDTO.getNumLote())
                .build();

        inventarioRepository.save(inventario);

        return InventarioDTO.builder()
                .id(inventario.getId())
                .stock(inventario.getStock())
                .entrada(inventario.getEntrada())
                .salida(inventario.getSalida())
                .agotado(inventario.isAgotado())
                .numLote(inventario.getNumLote())
                .fechaCreacion(inventario.getFechaCreacion())
                .fechaActualizacion(inventario.getFechaActualizacion())
                .libroCardDTO(LibroCardDTO.builder()
                        .id(inventario.getLibro().getId())
                        .titulo(inventario.getLibro().getTitulo())
                        .autor(inventario.getLibro().getAutor())
                        .isbn(inventario.getLibro().getIsbn())
                        .precio(inventario.getLibro().getPrecio())
                        .descuento(inventario.getLibro().getDescuento())
                        .descripcion(inventario.getLibro().getDescripcion())
                        .imgPortada(inventario.getLibro().getImgPortada())
                        .build())
                .build();
    }

    @Override
    public InventarioDTO actualizarInventario(Long id, InventarioDTO inventarioDTO){
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error al encontrar el inventario del libro con el ID: " + id));

        Integer nuevaEntrada = inventarioDTO.getEntrada();
        Integer nuevaSalida = inventarioDTO.getSalida();
        Integer totalSalidas = inventario.getSalida() + nuevaSalida;
        int actualizarStock = nuevaEntrada - totalSalidas;

        /* Validar que no se retire más stock del disponible
        if (nuevaSalida > inventario.getStock()) {
            throw new IllegalArgumentException("No hay suficiente stock para procesar la salida.");
        }*/

        inventario.setStock(actualizarStock);
        inventario.setSalida(totalSalidas);
        inventario.setEntrada(nuevaEntrada);
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventario.setNumLote(inventarioDTO.getNumLote()!= null ? inventarioDTO.getNumLote() : inventario.getNumLote());

        // Establecer agotado si el stock llega a 0
        inventario.setAgotado(actualizarStock <= 0);

        inventarioRepository.save(inventario);

        return InventarioDTO.builder()
                .id(inventario.getId())
                .stock(inventario.getStock())
                .entrada(inventario.getEntrada())
                .salida(inventario.getSalida())
                .agotado(inventario.isAgotado())
                .numLote(inventario.getNumLote())
                .fechaCreacion(inventario.getFechaCreacion())
                .fechaActualizacion(inventario.getFechaActualizacion())
                .libroCardDTO(LibroCardDTO.builder()
                        .id(inventario.getLibro().getId())
                        .titulo(inventario.getLibro().getTitulo())
                        .autor(inventario.getLibro().getAutor())
                        .isbn(inventario.getLibro().getIsbn())
                        .precio(inventario.getLibro().getPrecio())
                        .descuento(inventario.getLibro().getDescuento())
                        .descripcion(inventario.getLibro().getDescripcion())
                        .imgPortada(inventario.getLibro().getImgPortada())
                        .build())
                .build();
    }

    @Override
    public void eliminarInventario(Long id){
        Optional<Inventario> inventario = inventarioRepository.findById(id);

        if (inventario.isPresent()){
            inventarioRepository.deleteById(inventario.get().getId());
        } else {
            throw new EntityNotFoundException("Inventario no encontrado");
        }
    }
}
