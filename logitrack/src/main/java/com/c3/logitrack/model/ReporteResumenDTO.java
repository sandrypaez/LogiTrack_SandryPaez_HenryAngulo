package com.c3.logitrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResumenDTO {
    
    // Stock total por bodega
    private List<Map<String, Object>> stockPorBodega;
    
    // Productos más movidos
    private List<Map<String, Object>> productosMasMovidos;
    
    // Resumen general
    private Long totalBodegas;
    private Long totalProductos;
    private Long totalMovimientos;
    private Long totalAuditorias;
}