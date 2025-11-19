package com.c3.logitrack.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("encodedPassword");
        usuario.setRol(RolUsuario.EMPLEADO);
    }

    @Test
    void authenticateUser_Exitoso() {
        // Given
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // When
        Usuario resultado = authService.authenticateUser("testuser", "password123");

        // Then
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        verify(usuarioRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test
    void authenticateUser_UsuarioNoEncontrado() {
        // Given
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser("noexiste", "password123");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findByUsername("noexiste");
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void authenticateUser_PasswordIncorrecta() {
        // Given
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser("testuser", "wrongpassword");
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(usuarioRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
    }

    @Test
    void registerUser_Exitoso() {
        // Given
        when(usuarioRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedNewPassword");

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(2L);
        nuevoUsuario.setUsername("newuser");
        nuevoUsuario.setEmail("new@test.com");
        nuevoUsuario.setPassword("encodedNewPassword");
        nuevoUsuario.setRol(RolUsuario.EMPLEADO);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(nuevoUsuario);

        // When
        Usuario resultado = authService.registerUser("newuser", "new@test.com", "password123", RolUsuario.EMPLEADO);

        // Then
        assertNotNull(resultado);
        assertEquals("newuser", resultado.getUsername());
        assertEquals("new@test.com", resultado.getEmail());
        assertEquals(RolUsuario.EMPLEADO, resultado.getRol());
        verify(usuarioRepository).findByUsername("newuser");
        verify(usuarioRepository).findByEmail("new@test.com");
        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registerUser_UsuarioYaExiste() {
        // Given
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser("testuser", "new@test.com", "password123", RolUsuario.EMPLEADO);
        });

        assertEquals("El username ya está en uso", exception.getMessage());
        verify(usuarioRepository).findByUsername("testuser");
        verify(usuarioRepository, never()).findByEmail(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registerUser_EmailYaExiste() {
        // Given
        when(usuarioRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser("newuser", "test@test.com", "password123", RolUsuario.EMPLEADO);
        });

        assertEquals("El email ya está en uso", exception.getMessage());
        verify(usuarioRepository).findByUsername("newuser");
        verify(usuarioRepository).findByEmail("test@test.com");
        verify(usuarioRepository, never()).save(any());
    }
}