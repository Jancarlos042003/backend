package com.example.backend.service;

import com.example.backend.dto.SubcategoriaDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Categoria;
import com.example.backend.model.Subcategoria;
import com.example.backend.repository.CategoriaRepository;
import com.example.backend.repository.SubcategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcategoriaServiceImpl implements SubcategoriaService {
    @Autowired
    SubcategoriaRepository subcategoriaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    public Subcategoria crearSubcategoria(SubcategoriaDTO subcategoria){
        Categoria categoria = categoriaRepository.findById(subcategoria.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el ID: " + subcategoria.getId()));

        Subcategoria nuevaSubcategoria = Subcategoria.builder()
                .nombre(subcategoria.getNombre())
                .categoria(categoria)
                .build();
        return subcategoriaRepository.save(nuevaSubcategoria);
    }

    @Override
    public List<Subcategoria> mostrarSubcategorias(){
        return subcategoriaRepository.findAll();
    }

    @Override
    public void eliminarSubategoria(Long id){
        Optional<Subcategoria> subcategoriaOptional = subcategoriaRepository.findById(id);

        if (subcategoriaOptional.isPresent()){
            subcategoriaRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Subcategoria con ID: " + id + " no encontrada.");
        }
    }
}
