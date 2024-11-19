package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Rol;
import com.example.backend.repository.RolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Verificar el funcionamiento del Service de Rol")
public class RolServiceImplTest {
    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    @Test
    @DisplayName("Retorna el nuevo rol creado")
    void testCrearRol(){
        // ARRANGE
        Rol rol = new Rol(1L, "Rol 1");
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        // ACT
        Rol nuevoRol = rolService.crearRol(rol);

        // ASSERT
        assertAll(
                () -> assertNotNull(nuevoRol),
                () -> assertEquals(1L, nuevoRol.getId()),
                () -> assertEquals("Rol 1", nuevoRol.getNombre())
        );

        verify(rolRepository, times(1)).save(any(Rol.class));
    }

    @Test
    @DisplayName("Elimina un rol que existe")
    void testEliminarRol(){
        // ARRANGE
        Long id = 1L;
        Rol rol = new Rol(1L, "Rol 1");

        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));

        // ACT
        rolService.eliminarRol(id);

        // ASSERT
        verify(rolRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Retorna error al tratar eliminar un libro que no existe")
    void testEliminarRolNoEncontrado(){
        // ARRANGE
        Long id = 1L;

        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        // ACT-ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rolService.eliminarRol(id));

        assertAll(
                () -> assertEquals("Rol no encontrado con el ID:" + id, exception.getMessage())
        );
        verify(rolRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna un lista de todos los roles")
    void mostrarRoles(){
        // ARRANGE
        List<Rol> mostrarRoles = DataProvider.listaRolesMock();
        when(rolRepository.findAll()).thenReturn(mostrarRoles);

        // ACT
        List<Rol> respuesta = rolService.mostrarRoles();

        // ASSERT
        assertAll(
                () -> assertNotNull(respuesta),
                () -> assertEquals(2, respuesta.size())
        );

        verify(rolRepository, times(1)).findAll();
    }
}
