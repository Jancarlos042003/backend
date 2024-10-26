package com.example.backend.service;

import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Inventario;
import com.example.backend.model.Libro;
import com.example.backend.repository.InventarioRepository;
import com.example.backend.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {
    @Autowired
    InventarioRepository inventarioRepository;

    @Autowired
    LibroRepository libroRepository;

    @Override
    public List<Inventario> mostrarInventarios(){
        return inventarioRepository.findAll();
    }

    @Override
    public Inventario mostrarInventario(Long id){
        Inventario inventarioExistente = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado"));
        return inventarioExistente;
    }

    @Override
    public Inventario crearInventario(Inventario inventario){
        Libro libro = libroRepository.findById(inventario.getLibro().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        inventario.setLibro(libro);
        inventario.setFechaActualizacion(LocalDate.now());

        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizarInventario(Long id, Inventario inventario){
        Inventario inventarioExistente = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado"));

        inventarioExistente.setCantidad(inventario.getCantidad());
        inventarioExistente.setFechaActualizacion(LocalDate.now());

        return inventarioRepository.save(inventarioExistente);
    }

    @Override
    public void eliminarInventario(Long id){
        Optional<Inventario> inventarioOptional = inventarioRepository.findById(id);

        if (inventarioOptional.isPresent()){
            inventarioRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Inventario no encontrado con el ID: " +id);
        }
    }
}
