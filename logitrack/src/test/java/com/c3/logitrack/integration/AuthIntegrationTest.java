package com.c3.logitrack.integration;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class AuthIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        
        usuarioRepository.deleteAll();
    }

    @Test
    void integrationTest_LoginExitoso() throws Exception {
        // Given - Crear usuario de prueba en la base de datos
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword(passwordEncoder.encode("password123"));
        usuario.setRol(RolUsuario.EMPLEADO);
        usuarioRepository.save(usuario);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "password123");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inicio de sesión exitoso"))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.usuario").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.rol").value("EMPLEADO"));
    }

    @Test
    void integrationTest_LoginCredencialesIncorrectas() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword(passwordEncoder.encode("password123"));
        usuario.setRol(RolUsuario.EMPLEADO);
        usuarioRepository.save(usuario);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "wrongpassword");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void integrationTest_RegistroExitoso() throws Exception {
        // Given
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("username", "newuser");
        registerRequest.put("email", "newuser@test.com");
        registerRequest.put("password", "password123");
        registerRequest.put("rol", "EMPLEADO");

        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario registrado correctamente"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"));

        // Verificar que el usuario fue guardado en la base de datos
        org.junit.jupiter.api.Assertions.assertTrue(usuarioRepository.findByUsername("newuser").isPresent());
    }

    @Test
    void integrationTest_RegistroUsuarioDuplicado() throws Exception {
        // Given - Crear usuario existente
        Usuario usuario = new Usuario();
        usuario.setUsername("existinguser");
        usuario.setEmail("existing@test.com");
        usuario.setPassword(passwordEncoder.encode("password123"));
        usuario.setRol(RolUsuario.EMPLEADO);
        usuarioRepository.save(usuario);

        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("username", "existinguser");
        registerRequest.put("email", "newemail@test.com");
        registerRequest.put("password", "password123");
        registerRequest.put("rol", "EMPLEADO");

        // When & Then
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El username ya está en uso"));
    }
}