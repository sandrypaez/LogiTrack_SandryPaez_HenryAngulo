package com.c3.logitrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.c3.logitrack.entities.Bodega;
import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.entities.Producto;
import com.c3.logitrack.entities.enums.TipoMovimiento;
import com.c3.logitrack.exception.BadRequestException;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.model.MovimientoRequest;
import com.c3.logitrack.repository.BodegaRepository;
import com.c3.logitrack.repository.MovimientoRepository;
import com.c3.logitrack.repository.ProductoRepository;
import com.c3.logitrack.repository.UsuarioRepository;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private BodegaRepository bodegaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ==========================
    // CREAR MOVIMIENTO
    // ==========================
    @SuppressWarnings("null")
    @Transactional
    public Movimiento crearMovimiento(MovimientoRequest request) {
        // Validaciones básicas
        if (request.getProductoId() == null)
            throw new BadRequestException("Producto obligatorio");
        if (request.getCantidad() <= 0)
            throw new BadRequestException("Cantidad debe ser mayor que 0");
        if (request.getTipoMovimiento() == null)
            throw new BadRequestException("Tipo de movimiento obligatorio");

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Bodega origen = null;
        Bodega destino = null;

        TipoMovimiento tipo;
        try {
            tipo = TipoMovimiento.valueOf(request.getTipoMovimiento());
        } catch (Exception e) {
            throw new BadRequestException("Tipo de movimiento inválido");
        }

        // Validaciones específicas
        switch (tipo) {
            case ENTRADA:
                if (request.getBodegaDestinoId() == null)
                    throw new BadRequestException("Bodega destino obligatoria para ENTRADA");
                destino = bodegaRepository.findById(request.getBodegaDestinoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega destino no encontrada"));
                break;

            case SALIDA:
                if (request.getBodegaOrigenId() == null)
                    throw new BadRequestException("Bodega origen obligatoria para SALIDA");
                origen = bodegaRepository.findById(request.getBodegaOrigenId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega origen no encontrada"));
                if (producto.getStock() < request.getCantidad())
                    throw new BadRequestException("Stock insuficiente en producto");
                break;

            case TRANSFERENCIA:
                if (request.getBodegaOrigenId() == null || request.getBodegaDestinoId() == null)
                    throw new BadRequestException("Bodegas origen y destino obligatorias para TRANSFERENCIA");
                origen = bodegaRepository.findById(request.getBodegaOrigenId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega origen no encontrada"));
                destino = bodegaRepository.findById(request.getBodegaDestinoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega destino no encontrada"));
                if (origen.getId().equals(destino.getId()))
                    throw new BadRequestException("No se puede transferir dentro de la misma bodega");
                if (producto.getStock() < request.getCantidad())
                    throw new BadRequestException("Stock insuficiente en producto");
                break;
        }

        // Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(tipo);
        movimiento.setProducto(producto);
        movimiento.setCantidad(request.getCantidad());
        movimiento.setBodegaOrigen(origen);
        movimiento.setBodegaDestino(destino);
        movimiento.setFecha(request.getFecha() != null 
                ? request.getFecha() 
                : LocalDateTime.now());

        // Usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            usuarioRepository.findByUsername(auth.getName()).ifPresent(movimiento::setUsuarioResponsable);
        }

        // Actualizar stock
        switch (tipo) {
            case ENTRADA:
                producto.setStock(producto.getStock() + request.getCantidad());
                break;
            case SALIDA:
            case TRANSFERENCIA:
                producto.setStock(producto.getStock() - request.getCantidad());
                break;
        }
        productoRepository.save(producto);

        return movimientoRepository.save(movimiento);
    }

    // ==========================
    // LISTAR MOVIMIENTOS
    // ==========================
    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento obtenerMovimientoPorId(Long id) {
        if (id == null)
            throw new BadRequestException("El id del movimiento es obligatorio");
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));
    }

    public List<Movimiento> obtenerMovimientosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null)
            throw new BadRequestException("Las fechas de inicio y fin son obligatorias");
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @SuppressWarnings("null")
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = obtenerMovimientoPorId(id);
        movimientoRepository.delete(movimiento);
    }
}
