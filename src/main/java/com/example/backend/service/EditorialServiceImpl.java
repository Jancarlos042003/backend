package com.example.backend.service;

import com.example.backend.dto.EditorialDTO;
import com.example.backend.model.Editorial;
import com.example.backend.repository.EditorialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditorialServiceImpl implements EditorialService {
    @Autowired
    EditorialRepository editorialRepository;

    @Override
    public Editorial crearEditorial(EditorialDTO editorial){
        Editorial nuevoEditorial = Editorial.builder()
                .nombre(editorial.getNombre())
                .build();
        return editorialRepository.save(nuevoEditorial);
    }

    @Override
    public List<Editorial> mostrarEditoriales(){
        return editorialRepository.findAll();
    }

    @Override
    public void eliminarEditorial(Long id){
        Optional<Editorial> editorialOptional = editorialRepository.findById(id);

        if (editorialOptional.isPresent()){
            editorialRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Editorial no encontrado con el ID: "+ id);
        }
    }
}
