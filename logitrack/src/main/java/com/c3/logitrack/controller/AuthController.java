package com.c3.logitrack.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.model.LoginRequest;
import com.c3.logitrack.security.JwtTokenProvider;
import com.c3.logitrack.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con sus credenciales y retorna un token JWT para acceder a endpoints protegidos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso - Token JWT generado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Inicio de sesión exitoso\",\n" +
                                    "  \"token\": \"eyJhbGciOiJIUzUxMiJ9...\",\n" +
                                    "  \"type\": \"Bearer\",\n" +
                                    "  \"usuario\": \"admin\",\n" +
                                    "  \"email\": \"admin@logitrack.com\",\n" +
                                    "  \"rol\": \"ADMIN\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o parámetros faltantes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Usuario o contraseña incorrectos\"\n" +
                                    "}")))
    })
    public ResponseEntity<?> login(
            @Valid @RequestBody @Parameter(description = "Credenciales del usuario") LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Username y password son obligatorios");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            Usuario usuario = authService.authenticateUser(username, password);
            String token = jwtTokenProvider.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inicio de sesión exitoso");
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("usuario", usuario.getUsername());
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Registra un nuevo usuario en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Usuario registrado correctamente\",\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"username\": \"empleado1\",\n" +
                                    "  \"email\": \"empleado@logitrack.com\",\n" +
                                    "  \"rol\": \"EMPLEADO\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"El usuario ya existe\"\n" +
                                    "}")))
    })
    public ResponseEntity<?> register(
            @RequestBody @Parameter(description = "Datos para registrar usuario") Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String rolStr = request.get("rol");

        if (username == null || password == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Username y password son obligatorios");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            RolUsuario rol = null;
            if (rolStr != null) {
                try {
                    rol = RolUsuario.valueOf(rolStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    rol = RolUsuario.EMPLEADO; // Rol por defecto
                }
            }

            Usuario nuevo = authService.registerUser(username, email, password, rol);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado correctamente");
            response.put("id", nuevo.getId());
            response.put("username", nuevo.getUsername());
            response.put("email", nuevo.getEmail());
            response.put("rol", nuevo.getRol());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
