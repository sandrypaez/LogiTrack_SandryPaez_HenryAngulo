package com.c3.logitrack.entities;

import com.c3.logitrack.entities.enums.TipoOperacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacion tipoOperacion;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String entidadAfectada;

    private Long entidadId;

    @Column(columnDefinition = "TEXT")
    private String valoresAnteriores;

    @Column(columnDefinition = "TEXT")
    private String valoresNuevos;

    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}