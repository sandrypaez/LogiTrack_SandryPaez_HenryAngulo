package com.c3.logitrack.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.model.MovimientoRequest;
import com.c3.logitrack.service.MovimientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movimientos")
@Tag(name = "Movimientos", description = "Gestión de movimientos de inventario")
@SecurityRequirement(name = "bearerAuth")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    // LISTAR TODOS
    @GetMapping
    @Operation(summary = "Listar todos los movimientos")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<List<Movimiento>> listarMovimientos() {
        return ResponseEntity.ok(movimientoService.listarMovimientos());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
        @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    public ResponseEntity<Movimiento> obtenerMovimiento(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientoPorId(id));
    }

    // FILTRAR POR FECHAS
    @GetMapping("/fechas")
    @Operation(summary = "Movimientos por rango de fechas")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorFechas(fechaInicio, fechaFin));
    }

    // CREAR MOVIMIENTO
    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @Operation(summary = "Crear un nuevo movimiento")
    public ResponseEntity<Movimiento> crearMovimiento(@Valid @RequestBody MovimientoRequest request) {
        Movimiento movimiento = movimientoService.crearMovimiento(request);
        return new ResponseEntity<>(movimiento, HttpStatus.CREATED);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar movimiento")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
