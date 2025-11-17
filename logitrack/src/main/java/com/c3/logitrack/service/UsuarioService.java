package com.c3.logitrack.service;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @SuppressWarnings("null")
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @SuppressWarnings("null")
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Otros métodos según la lógica de negocio (login, registro, actualización etc)
}