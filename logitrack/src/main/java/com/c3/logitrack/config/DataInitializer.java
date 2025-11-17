package com.c3.logitrack.config;

import com.c3.logitrack.entities.Usuario;
import com.c3.logitrack.entities.enums.RolUsuario;
import com.c3.logitrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario admin si no existe
        if (!usuarioRepository.findByUsername("admin").isPresent()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@logitrack.com");
            admin.setPassword(passwordEncoder.encode("1234")); // Contraseña: 1234
            admin.setRol(RolUsuario.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario admin creado - Usuario: admin, Contraseña: 1234");
        }

        // Crear usuario empleado si no existe
        if (!usuarioRepository.findByUsername("empleado1").isPresent()) {
            Usuario empleado = new Usuario();
            empleado.setUsername("empleado1");
            empleado.setEmail("empleado1@logitrack.com");
            empleado.setPassword(passwordEncoder.encode("abcd")); // Contraseña: abcd
            empleado.setRol(RolUsuario.EMPLEADO);
            usuarioRepository.save(empleado);
            System.out.println("✅ Usuario empleado1 creado - Usuario: empleado1, Contraseña: abcd");
        }
    }
}

