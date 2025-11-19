package com.c3.logitrack.entities;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UsuarioValidationTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void usuarioValido_NoDebeGenerarViolaciones() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("password123");

        // When
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void usernameVacio_DebeGenerarViolacion() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("");
        usuario.setEmail("test@test.com");
        usuario.setPassword("password123");

        // When
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void emailInvalido_DebeGenerarViolacion() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("email-invalido");
        usuario.setPassword("password123");

        // When
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void passwordVacia_DebeGenerarViolacion() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("");

        // When
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
}