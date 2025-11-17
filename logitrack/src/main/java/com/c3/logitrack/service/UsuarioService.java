package com.c3.logitrack.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.exception.BadRequestException;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario getUsuarioById(Long id) {
        if (id == null) {
            throw new BadRequestException("El id del usuario es obligatorio");
        }
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public Usuario createUsuario(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuario es requerido");
        return usuarioRepository.save(usuario);
    }

    // Otros métodos según la lógica de negocio (login, registro, actualización etc)
}