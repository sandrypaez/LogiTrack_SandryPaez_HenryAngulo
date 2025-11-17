package com.c3.logitrack.entities;

import com.c3.logitrack.entities.enums.RolUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El username es obligatorio")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol = RolUsuario.EMPLEADO; // Rol por defecto

}