package com.c3.logitrack.security;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "LogiTrackSecretKeyForJWTTokenGeneration2024SecureKeyMustBeAtLeast32Characters");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 86400000L); // 24 horas
    }

    @Test
    void generateToken_DebeGenerarTokenValido() {
        // Given
        String username = "testuser";

        // When
        String token = jwtTokenProvider.generateToken(username);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void getUsernameFromToken_DebeExtraerUsernameCorrectamente() {
        // Given
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);

        // When
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void getExpirationDateFromToken_DebeRetornarFechaExpiracion() {
        // Given
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);

        // When
        Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);

        // Then
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date())); // Debe ser en el futuro
    }

    @Test
    void validateToken_ConTokenValidoYUsuario_DebeRetornarTrue() {
        // Given
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);
        when(userDetails.getUsername()).thenReturn(username);

        // When
        Boolean isValid = jwtTokenProvider.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_ConUsuarioIncorrecto_DebeRetornarFalse() {
        // Given
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);
        when(userDetails.getUsername()).thenReturn("otheruser");

        // When
        Boolean isValid = jwtTokenProvider.validateToken(token, userDetails);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_SoloToken_ConTokenValido_DebeRetornarTrue() {
        // Given
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);

        // When
        Boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_ConTokenInvalido_DebeRetornarFalse() {
        // Given
        String tokenInvalido = "token.invalido.jwt";

        // When
        Boolean isValid = jwtTokenProvider.validateToken(tokenInvalido);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_ConTokenVacio_DebeRetornarFalse() {
        // Given
        String tokenVacio = "";

        // When
        Boolean isValid = jwtTokenProvider.validateToken(tokenVacio);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_ConTokenNull_DebeRetornarFalse() {
        // When
        Boolean isValid = jwtTokenProvider.validateToken(null);

        // Then
        assertFalse(isValid);
    }
}