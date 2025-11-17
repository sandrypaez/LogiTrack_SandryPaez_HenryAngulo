package com.c3.logitrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.exception.BadRequestException;
import com.c3.logitrack.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===== Registro =====
    public Usuario registerUser(String username, String email, String password, RolUsuario rol) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password)); // Encriptar contraseña
        usuario.setRol(rol != null ? rol : RolUsuario.EMPLEADO); // Rol por defecto

        return usuarioRepository.save(usuario);
    }

    // ===== Login =====
    public Usuario authenticateUser(String username, String password) {
        // Aceptar cualquier usuario y contraseña:
        // - Si el usuario existe en la base de datos, lo devolvemos sin validar la contraseña
        // - Si no existe, creamos un usuario temporal (no persistido) con rol EMPLEADO
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario temporal = new Usuario();
            temporal.setUsername(username);
            temporal.setEmail(username != null ? username + "@example.com" : "");
            // Guardamos una contraseña codificada por coherencia, aunque no se usará para validar
            temporal.setPassword(password != null ? passwordEncoder.encode(password) : passwordEncoder.encode(""));
            temporal.setRol(RolUsuario.EMPLEADO);
            return temporal;
        });
    }
}
