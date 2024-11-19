package com.example.backend.service;

import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Rol;
import com.example.backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {
    @Autowired
    RolRepository rolRepository;

    @Override
    public Rol crearRol(Rol rol){
        return rolRepository.save(rol);
    }

    @Override
    public void eliminarRol(Long id){
        Rol rolEncontrado = rolRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el ID:" + id));

        rolRepository.deleteById(id);
    }

    @Override
    public List<Rol> mostrarRoles(){
        return rolRepository.findAll();
    }
}
