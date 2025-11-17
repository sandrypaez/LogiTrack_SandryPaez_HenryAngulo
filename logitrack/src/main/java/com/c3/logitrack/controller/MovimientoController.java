package com.c3.logitrack.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.service.MovimientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movimientos")
@Tag(name = "Movimientos", description = "Gestión de movimientos de inventario (entradas, salidas, transferencias)")
@SecurityRequirement(name = "bearerAuth")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    @Operation(
        summary = "Listar todos los movimientos",
        description = "Obtiene una lista completa de todos los movimientos de inventario"
    )
    @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Movimiento>> listarMovimientos() {
        List<Movimiento> movimientos = movimientoService.listarMovimientos();
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener movimiento por ID",
        description = "Obtiene los detalles de un movimiento específico usando su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    public ResponseEntity<Movimiento> obtenerMovimiento(
            @Parameter(description = "ID del movimiento", required = true)
            @PathVariable Long id) {
        Movimiento movimiento = movimientoService.obtenerMovimientoPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    @GetMapping("/fechas")
    @Operation(
        summary = "Movimientos por rango de fechas",
        description = "Obtiene todos los movimientos dentro de un rango de fechas especificado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de movimientos en el rango de fechas",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorFechas(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-ddTHH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-ddTHH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("/entrada")
    @Operation(
        summary = "Registrar entrada de producto",
        description = "Registra una entrada (recepción) de productos a una bodega"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrada registrada exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN o EMPLEADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Movimiento> registrarEntrada(
            @Parameter(description = "Datos del movimiento de entrada")
            @Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.registrarEntrada(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PostMapping("/salida")
    @Operation(
        summary = "Registrar salida de producto",
        description = "Registra una salida (envío) de productos desde una bodega"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Salida registrada exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN o EMPLEADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Movimiento> registrarSalida(
            @Parameter(description = "Datos del movimiento de salida")
            @Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.registrarSalida(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PostMapping("/transferencia")
    @Operation(
        summary = "Registrar transferencia entre bodegas",
        description = "Registra una transferencia de productos de una bodega a otra"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferencia registrada exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN o EMPLEADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Movimiento> registrarTransferencia(
            @Parameter(description = "Datos del movimiento de transferencia")
            @Valid @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.registrarTransferencia(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar movimiento",
        description = "Elimina un movimiento del sistema (solo requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(description = "ID del movimiento a eliminar", required = true)
            @PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
