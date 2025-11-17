package com.c3.logitrack.controller;

import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@Tag(name = "Movimientos", description = "Endpoints para gestión de movimientos de inventario")
@SecurityRequirement(name = "bearerAuth")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    @Operation(summary = "Listar todos los movimientos", description = "Obtiene una lista de todos los movimientos")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Movimiento>> listarMovimientos() {
        List<Movimiento> movimientos = movimientoService.listarMovimientos();
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID", description = "Obtiene un movimiento específico por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Movimiento> obtenerMovimiento(@PathVariable Long id) {
        Movimiento movimiento = movimientoService.obtenerMovimientoPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    @GetMapping("/fechas")
    @Operation(summary = "Movimientos por rango de fechas", description = "Obtiene movimientos entre dos fechas")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorFechas(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("/entrada")
    @Operation(summary = "Registrar entrada", description = "Registra una entrada de productos a una bodega")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrada registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Movimiento> registrarEntrada(@Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.registrarEntrada(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PostMapping("/salida")
    @Operation(summary = "Registrar salida", description = "Registra una salida de productos de una bodega")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Movimiento> registrarSalida(@Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.registrarSalida(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Registrar transferencia", description = "Registra una transferencia de productos entre bodegas")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Movimiento> registrarTransferencia(@Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.registrarTransferencia(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar movimiento", description = "Elimina un movimiento del sistema (solo ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
