package com.c3.logitrack.entities;

import java.math.BigDecimal;

import com.c3.logitrack.config.AuditoriaListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@EntityListeners(AuditoriaListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
    private String categoria;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 999999, message = "El stock no puede exceder 999,999 unidades")
    private Integer stock = 0;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal precio;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @Column(name = "stock_minimo")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo = 0;

    @Pattern(regexp = "^[A-Z0-9-]*$", message = "El código SKU solo puede contener letras mayúsculas, números y guiones")
    @Size(max = 50, message = "El código SKU no puede exceder 50 caracteres")
    @Column(name = "sku", unique = true)
    private String sku;

    // Métodos de utilidad
    public boolean isBajoStock() {
        return this.stock != null && this.stockMinimo != null && this.stock <= this.stockMinimo;
    }

    public boolean isStockSuficiente(Integer cantidadRequerida) {
        return this.stock != null && cantidadRequerida != null && this.stock >= cantidadRequerida;
    }
}