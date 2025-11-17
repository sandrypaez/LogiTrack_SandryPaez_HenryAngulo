package com.c3.logitrack.entities;

import com.c3.logitrack.config.AuditoriaListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bodegas")
@EntityListeners(AuditoriaListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bodega {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la bodega es obligatorio")
    private String nombre;

    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidad;

    @NotBlank(message = "El encargado es obligatorio")
    private String encargado;
}
