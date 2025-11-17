package com.c3.logitrack.controller;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.security.JwtTokenProvider;
import com.c3.logitrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ===== LOGIN =====
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
    })
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

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

    // ===== REGISTRO =====
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados")
    })
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
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
