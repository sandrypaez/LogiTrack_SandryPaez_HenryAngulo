package com.c3.logitrack.service;

import com.c3.logitrack.model.ReporteResumenDTO;
import com.c3.logitrack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private BodegaRepository bodegaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public ReporteResumenDTO obtenerResumen() {
        ReporteResumenDTO resumen = new ReporteResumenDTO();

        // Stock total por bodega
        List<Map<String, Object>> stockPorBodega = calcularStockPorBodega();
        resumen.setStockPorBodega(stockPorBodega);

        // Productos más movidos
        List<Map<String, Object>> productosMasMovidos = calcularProductosMasMovidos();
        resumen.setProductosMasMovidos(productosMasMovidos);

        // Resumen general
        resumen.setTotalBodegas(bodegaRepository.count());
        resumen.setTotalProductos(productoRepository.count());
        resumen.setTotalMovimientos(movimientoRepository.count());
        resumen.setTotalAuditorias(auditoriaRepository.count());

        return resumen;
    }

    private List<Map<String, Object>> calcularStockPorBodega() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        
        bodegaRepository.findAll().forEach(bodega -> {
            Map<String, Object> item = new HashMap<>();
            item.put("bodegaId", bodega.getId());
            item.put("bodegaNombre", bodega.getNombre());
            item.put("ubicacion", bodega.getUbicacion());
            
            // Calcular stock total de productos en esta bodega
            // Nota: Esto requiere una relación entre productos y bodegas, o usar movimientos
            // Por ahora, calculamos basado en movimientos
            Long stockTotal = movimientoRepository.findAll().stream()
                    .filter(m -> m.getBodegaDestino() != null && m.getBodegaDestino().getId().equals(bodega.getId()))
                    .mapToLong(m -> Long.valueOf(m.getCantidad()))
                    .sum();
            
            item.put("stockTotal", stockTotal);
            resultado.add(item);
        });
        
        return resultado;
    }

    private List<Map<String, Object>> calcularProductosMasMovidos() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        Map<Long, Long> movimientosPorProducto = new HashMap<>();
        
        movimientoRepository.findAll().forEach(movimiento -> {
            Long productoId = movimiento.getProducto().getId();
            movimientosPorProducto.put(productoId, 
                    movimientosPorProducto.getOrDefault(productoId, 0L) + movimiento.getCantidad());
        });
        
        movimientosPorProducto.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10) // Top 10 productos más movidos
                .forEach(entry -> {
                    productoRepository.findById(entry.getKey()).ifPresent(producto -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("productoId", producto.getId());
                        item.put("productoNombre", producto.getNombre());
                        item.put("categoria", producto.getCategoria());
                        item.put("totalMovimientos", entry.getValue());
                        resultado.add(item);
                    });
                });
        
        return resultado;
    }
}

