package com.example.backend.service;

import com.example.backend.DataProvider;
import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl   userDetailsService;

    @Test
    @DisplayName("Cargar un usuario por su username(email)")
    void testLoadUserByUsername(){
        // ARRANGE
        String username = "usuario@gmail.com";
        Usuario usuarioEncontrado = DataProvider.usuarioMock();
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuarioEncontrado));

        // ACT
        UserDetails resultado = userDetailsService.loadUserByUsername(username);

        // ASSERT
        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals("usuario@gmail.com", resultado.getUsername()),
                () -> assertEquals("encodedPassword", resultado.getPassword()),
                () -> assertTrue(resultado.isEnabled()),
                () -> assertTrue(resultado.isAccountNonExpired()),
                () -> assertTrue(resultado.isCredentialsNonExpired()),
                () -> assertTrue(resultado.isAccountNonLocked()),
                () -> assertTrue(resultado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")))
        );

        verify(usuarioRepository, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("Error al cargar un usuario. Username no encontrado")
    void testLoadUserByUsername_UsuarioNoEncontrado(){
        // ARRANGE
        String username = "noexiste@example.com";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // ACT
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        // ASSERT
        assertAll(
                () -> assertEquals("Usuario no encontrado con el email: " + username, exception.getMessage())
        );
        verify(usuarioRepository, times(1)).findByUsername(anyString());
    }
}
