package com.c3.logitrack.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoRequest {
    
    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El ID del producto debe ser positivo")
    private Long productoId;
    
    private Long bodegaOrigenId;
    
    private Long bodegaDestinoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que 0")
    @Max(value = 10000, message = "La cantidad no puede exceder 10000 unidades")
    private Integer cantidad;
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(ENTRADA|SALIDA|TRANSFERENCIA)$", message = "El tipo de movimiento debe ser ENTRADA, SALIDA o TRANSFERENCIA")
    private String tipoMovimiento;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    private LocalDateTime fecha;
}
