package com.c3.logitrack.service;

import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.entities.enums.TipoMovimiento;
import com.c3.logitrack.exception.BadRequestException;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.repository.MovimientoRepository;
import com.c3.logitrack.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento obtenerMovimientoPorId(Long id) {
        return movimientoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));
    }

    public List<Movimiento> obtenerMovimientosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Transactional
    public Movimiento registrarEntrada(Movimiento movimiento) {
        movimiento.setTipoMovimiento(TipoMovimiento.ENTRADA);
        
        // Validar que tenga bodega destino
        if (movimiento.getBodegaDestino() == null) {
            throw new BadRequestException("La bodega destino es obligatoria para una entrada");
        }
        
        // Actualizar stock del producto
        actualizarStockProducto(movimiento.getProducto().getId(), movimiento.getCantidad(), true);
        
        // Asignar usuario actual si no está asignado
        asignarUsuarioActual(movimiento);
        
        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public Movimiento registrarSalida(Movimiento movimiento) {
        movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
        
        // Validar que tenga bodega origen
        if (movimiento.getBodegaOrigen() == null) {
            throw new BadRequestException("La bodega origen es obligatoria para una salida");
        }
        
        // Validar stock suficiente
        validarStockSuficiente(movimiento.getProducto().getId(), movimiento.getCantidad());
        
        // Actualizar stock del producto (reducir)
        actualizarStockProducto(movimiento.getProducto().getId(), movimiento.getCantidad(), false);
        
        // Asignar usuario actual si no está asignado
        asignarUsuarioActual(movimiento);
        
        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public Movimiento registrarTransferencia(Movimiento movimiento) {
        movimiento.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);
        
        // Validar que tenga bodega origen y destino
        if (movimiento.getBodegaOrigen() == null || movimiento.getBodegaDestino() == null) {
            throw new BadRequestException("La bodega origen y destino son obligatorias para una transferencia");
        }
        
        if (movimiento.getBodegaOrigen().getId().equals(movimiento.getBodegaDestino().getId())) {
            throw new BadRequestException("La bodega origen y destino no pueden ser la misma");
        }
        
        // Validar stock suficiente en origen
        validarStockSuficiente(movimiento.getProducto().getId(), movimiento.getCantidad());
        
        // Actualizar stock del producto (reducir de origen, aumentar en destino)
        actualizarStockProducto(movimiento.getProducto().getId(), movimiento.getCantidad(), false);
        
        // Asignar usuario actual si no está asignado
        asignarUsuarioActual(movimiento);
        
        return movimientoRepository.save(movimiento);
    }

    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = obtenerMovimientoPorId(id);
        movimientoRepository.delete(movimiento);
    }

    private void actualizarStockProducto(Long productoId, Integer cantidad, boolean aumentar) {
        productoRepository.findById(productoId).ifPresent(producto -> {
            if (aumentar) {
                producto.setStock(producto.getStock() + cantidad);
            } else {
                producto.setStock(producto.getStock() - cantidad);
            }
            productoRepository.save(producto);
        });
    }

    private void validarStockSuficiente(Long productoId, Integer cantidad) {
        productoRepository.findById(productoId).ifPresent(producto -> {
            if (producto.getStock() < cantidad) {
                throw new BadRequestException("Stock insuficiente. Stock disponible: " + producto.getStock() + ", solicitado: " + cantidad);
            }
        });
    }

    private void asignarUsuarioActual(Movimiento movimiento) {
        if (movimiento.getUsuarioResponsable() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                // Aquí deberías obtener el usuario completo desde el repositorio
                // Por ahora, solo validamos que esté autenticado
            }
        }
    }
}