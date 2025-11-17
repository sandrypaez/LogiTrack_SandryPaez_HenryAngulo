package com.c3.logitrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.entities.Bodega;
import com.c3.logitrack.service.BodegaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/bodegas")
@Tag(name = "Bodegas", description = "Gestión de almacenes y depósitos")
@SecurityRequirement(name = "bearerAuth")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping
    @Operation(
        summary = "Listar todas las bodegas",
        description = "Obtiene una lista completa de todas las bodegas del sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de bodegas obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Bodega>> listarBodegas() {
        List<Bodega> bodegas = bodegaService.listarBodegas();
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener bodega por ID",
        description = "Obtiene los detalles de una bodega específica usando su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    public ResponseEntity<Bodega> obtenerBodega(
            @Parameter(description = "ID de la bodega", required = true)
            @PathVariable Long id) {
        Bodega bodega = bodegaService.obtenerBodegaPorId(id);
        return ResponseEntity.ok(bodega);
    }

    @PostMapping
    @Operation(
        summary = "Crear nueva bodega",
        description = "Crea una nueva bodega en el sistema (requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega creada exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bodega> crearBodega(
            @Parameter(description = "Datos de la bodega a crear")
            @Valid @RequestBody Bodega bodega) {
        Bodega nuevaBodega = bodegaService.crearBodega(bodega);
        return new ResponseEntity<>(nuevaBodega, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar bodega",
        description = "Actualiza los datos de una bodega existente (requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bodega> actualizarBodega(
            @Parameter(description = "ID de la bodega a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la bodega")
            @Valid @RequestBody Bodega bodega) {
        Bodega bodegaActualizada = bodegaService.actualizarBodega(id, bodega);
        return ResponseEntity.ok(bodegaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar bodega",
        description = "Elimina una bodega del sistema (requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarBodega(
            @Parameter(description = "ID de la bodega a eliminar", required = true)
            @PathVariable Long id) {
        bodegaService.eliminarBodega(id);
        return ResponseEntity.noContent().build();
    }
}
