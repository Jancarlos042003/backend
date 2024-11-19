package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.dto.CrearUsuarioDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Rol;
import com.example.backend.model.Usuario;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Retorna el usuario creado")
    void testCrearUsuario(){
        // ARRANGE(Configura los datos y el entorno de la prueba)
        CrearUsuarioDTO crearUsuarioDTO = new CrearUsuarioDTO();
        crearUsuarioDTO.setNombreCompleto("Usuario prueba");
        crearUsuarioDTO.setUsername("usuario@gmail.com");
        crearUsuarioDTO.setContrasenia("123456");
        crearUsuarioDTO.setRol(null); // Debe asignar USER

        Rol rol = DataProvider.listaRolesMock().get(0);

        Usuario usuarioGuardado = DataProvider.usuarioMock();

        when(rolRepository.findByNombre("USER")).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // ACT(Ejecuta la acción o el metodo en prueba)
        Usuario usuarioCreado = usuarioService.crearUsuario(crearUsuarioDTO);

        // ASSERT(Verifica el resultado esperado)
        assertAll(
                () -> assertNotNull(usuarioCreado),
                () -> assertEquals("Usuario prueba", usuarioCreado.getNombreCompleto()),
                () -> assertEquals("usuario@gmail.com", usuarioCreado.getUsername()),
                () -> assertEquals("encodedPassword", usuarioCreado.getContrasenia()),
                () -> assertEquals("USER", usuarioCreado.getRol().getNombre())
        );
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Retorna una lista de usuarios")
    void testMostrarUsuarios(){
        // ARRANGE
        when(usuarioRepository.findAll()).thenReturn(DataProvider.listaUsuariosMock());

        // ACT
        List<Usuario> resultado = usuarioRepository.findAll();

        // ASSER
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertFalse(resultado.isEmpty()),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Juan Peréz", resultado.get(0).getNombreCompleto()),
                () -> assertEquals("Cristiano Ronaldo", resultado.get(1).getNombreCompleto())
        );

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retorna el usuario buscado por su ID")
    void testMostrarUsuario(){
        // ARRANGE
        Long id = 2L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(DataProvider.usuarioMock()));

        // ACT
        Usuario usuarioEncontrado = usuarioService.mostrarUsuario(id);

        // ASSERT
        assertAll(
                () -> assertNotNull(usuarioEncontrado),
                () -> assertEquals("Usuario prueba", usuarioEncontrado.getNombreCompleto()),
                () -> assertEquals("usuario@gmail.com", usuarioEncontrado.getUsername()),
                () -> assertEquals("encodedPassword", usuarioEncontrado.getContrasenia())
        );

        verify(usuarioRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al mostrar un usuario. Su ID no existe")
    public void testMostrarUsuarioError(){
        // ARRANGE
        Long id = 3L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            Usuario resultado = usuarioService.mostrarUsuario(id);
        });

        // ASSERT
        assertAll(
                () -> assertEquals("Usuario no encontrado", exception.getMessage())
        );
        verify(usuarioRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retorna el usuario actualizado")
    void testActualizarUsuario(){
        // ARRANGE
        Long id = 1L;

        CrearUsuarioDTO usuarioDTO = new CrearUsuarioDTO();
        usuarioDTO.setNombreCompleto("Cristiano Ronaldo Dos Santos Aveiro");
        usuarioDTO.setUsername("cr7@gmail.com");
        usuarioDTO.setContrasenia("cr7");

        Usuario usuarioExistente = DataProvider.usuarioMock();

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(e -> e.getArgument(0));

        // ACT
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);

        // ASSERT
        assertAll(
                () -> assertNotNull(usuarioActualizado),
                () -> assertEquals("Cristiano Ronaldo Dos Santos Aveiro", usuarioActualizado.getNombreCompleto()),
                () -> assertEquals("cr7@gmail.com", usuarioActualizado.getUsername()),
                () -> assertEquals("encodedNewPassword", usuarioActualizado.getContrasenia())
        );

        verify(usuarioRepository, times(1)).findById(anyLong());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Elimina un usuario por su ID")
    void testEliminarUsuario(){
        // ARRANGE
        Long id = 2L;
        Usuario usuarioEncontrado = DataProvider.usuarioMock();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEncontrado));

        // ACT
        usuarioService.eliminarUsuario(id);

        // ASSERT
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class); // Para capturar el argumento de tipo Long
        verify(usuarioRepository).deleteById(longArgumentCaptor.capture());

        assertEquals(2L, longArgumentCaptor.getValue());

        verify(usuarioRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Error al eliminar un usuario. Su ID no existe")
    void testEliminarUsuario_UsuarioNoEncontrado(){
        // ARRANGE
        Long id = 6L;
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class , () -> usuarioService.eliminarUsuario(id));

        // ASSERT
        assertAll(
                () -> assertEquals("Usuario no encontrado con el ID: " + id, exception.getMessage())
        );
        verify(usuarioRepository, times(1)).findById(anyLong());
    }
}
