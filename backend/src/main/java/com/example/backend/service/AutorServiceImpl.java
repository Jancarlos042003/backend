package com.example.backend.service;

import com.example.backend.dto.AutorDTO;
import com.example.backend.model.Autor;
import com.example.backend.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorServiceImpl implements AutorService {
    @Autowired
    AutorRepository autorRepository;

    @Override
    public Autor crearAutor(AutorDTO autor){
        Autor nuevoAutor = Autor.builder()
                .nombre(autor.getNombre())
                .build();
        return autorRepository.save(nuevoAutor);
    }

    @Override
    public List<Autor> mostrarAutores(){
        return autorRepository.findAll();
    }

    @Override
    public void eliminarAutor(Long id){
        Optional<Autor> autorOptional = autorRepository.findById(id);

        if (autorOptional.isPresent()){
            autorRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Autor con ID " + id + " no encontrado.");
        }
    }
}
