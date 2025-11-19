package com.c3.logitrack.controller;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.model.LoginRequest;
import com.c3.logitrack.model.RegisterRequest;
import com.c3.logitrack.security.JwtTokenProvider;
import com.c3.logitrack.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginExitoso() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("admin", "admin123");
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setEmail("admin@test.com");
        usuario.setRol(RolUsuario.ADMIN);

        when(authService.authenticateUser("admin", "admin123")).thenReturn(usuario);
        when(jwtTokenProvider.generateToken("admin")).thenReturn("mocked-jwt-token");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inicio de sesión exitoso"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.usuario").value("admin"));
    }

    @Test
    void loginCredencialesInvalidas() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("admin", "wrongpassword");

        when(authService.authenticateUser("admin", "wrongpassword"))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void loginCamposVacios() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("", "");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.username").exists())
                .andExpect(jsonPath("$.validationErrors.password").exists());
    }

    @Test
    void registerExitoso() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("password123");
        registerRequest.setEmail("newuser@test.com");
        registerRequest.setRol(RolUsuario.EMPLEADO);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(1L);
        nuevoUsuario.setUsername("newuser");
        nuevoUsuario.setEmail("newuser@test.com");
        nuevoUsuario.setRol(RolUsuario.EMPLEADO);

        when(authService.registerUser("newuser", "newuser@test.com", "password123", RolUsuario.EMPLEADO))
                .thenReturn(nuevoUsuario);

        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario registrado correctamente"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"));
    }

    @Test
    void registerEmailInvalido() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("password123");
        registerRequest.setEmail("email-invalido");
        registerRequest.setRol(RolUsuario.EMPLEADO);

        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }
}