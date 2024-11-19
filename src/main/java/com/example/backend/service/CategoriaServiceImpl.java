package com.example.backend.service;

import com.example.backend.dto.CategoriaDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Categoria;
import com.example.backend.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    public Categoria crearCategoria(CategoriaDTO categoria){
        Categoria nuevaCategoria = Categoria.builder()
                .nombre(categoria.getNombre())
                .build();
        return categoriaRepository.save(nuevaCategoria);
    }

    @Override
    public Categoria mostrarCategoria(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el ID " + id));
    }

    @Override
    public List<Categoria> mostrarCategorias(){
        return categoriaRepository.findAll();
    }

    @Override
    public void eliminarCategoria(Long id){
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);

        if (categoriaOptional.isPresent()){
            categoriaRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Categoria no encontrada con el ID: " + id);
        }
    }
}
