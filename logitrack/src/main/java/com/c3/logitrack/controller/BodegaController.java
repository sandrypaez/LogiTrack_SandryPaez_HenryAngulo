package com.c3.logitrack.controller;

import com.c3.logitrack.entities.Bodega;
import com.c3.logitrack.service.BodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@Tag(name = "Bodegas", description = "Endpoints para gestión de bodegas")
@SecurityRequirement(name = "bearerAuth")
public class BodegaController {

    private final BodegaService bodegaService;


    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las bodegas", description = "Obtiene una lista de todas las bodegas")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Bodega>> listarBodegas() {
        List<Bodega> bodegas = bodegaService.listarBodegas();
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bodega por ID", description = "Obtiene una bodega específica por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Bodega> obtenerBodega(@PathVariable Long id) {
        Bodega bodega = bodegaService.obtenerBodegaPorId(id);
        return ResponseEntity.ok(bodega);
    }

    @PostMapping
    @Operation(summary = "Crear nueva bodega", description = "Crea una nueva bodega en el sistema")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Bodega> crearBodega(@Valid @RequestBody Bodega bodega) {
        Bodega nuevaBodega = bodegaService.crearBodega(bodega);
        return new ResponseEntity<>(nuevaBodega, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bodega", description = "Actualiza una bodega existente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bodega> actualizarBodega(@PathVariable Long id, @Valid @RequestBody Bodega bodega) {
        Bodega bodegaActualizada = bodegaService.actualizarBodega(id, bodega);
        return ResponseEntity.ok(bodegaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bodega", description = "Elimina una bodega del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarBodega(@PathVariable Long id) {
        bodegaService.eliminarBodega(id);
        return ResponseEntity.noContent().build();
    }
}
