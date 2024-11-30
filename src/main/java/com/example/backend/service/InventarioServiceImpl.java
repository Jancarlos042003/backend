package com.example.backend.service;

import com.example.backend.dto.InventarioDTO;
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
                .map(inventario -> modelMapper.map(inventario, InventarioDTO.class))
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
    public Inventario crearInventario(InventarioDTO inventarioDTO){
        Libro libro = libroRepository.findById(inventarioDTO.getIdLibro())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        Inventario inventario = Inventario.builder()
                .libro(libro)
                .stock(inventarioDTO.getStock())
                .fechaCreacion(LocalDate.now())
                .fechaActualizacion(LocalDateTime.now())
                .entrada(inventarioDTO.getEntrada())
                .salida(0)
                .agotado(inventarioDTO.isAgotado())
                .numLote(inventarioDTO.getNumLote())
                .build();

        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizarInventario(Long id, InventarioDTO inventarioDTO){
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error al encontrar el inventario del libro con el ID: " + id));

        int actualizarSalida = inventario.getSalida() + inventarioDTO.getSalida();

        inventario.setSalida(actualizarSalida);
        inventario.setStock(inventarioDTO.getStock());
        inventario.setFechaActualizacion(LocalDateTime.now());

        return inventarioRepository.save(inventario);
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
