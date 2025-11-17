package com.c3.logitrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.entities.Auditoria;
import com.c3.logitrack.entities.enums.TipoOperacion;
import com.c3.logitrack.service.AuditoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auditorias")
@CrossOrigin(origins = "*")
@Tag(name = "Auditoría", description = "Consulta de registros de auditoría y trazabilidad del sistema")
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    @Operation(
        summary = "Listar todas las auditorías",
        description = "Obtiene una lista completa de todos los registros de auditoría del sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de auditorías obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Auditoria>> listarAuditorias() {
        return ResponseEntity.ok(auditoriaService.listarAuditorias());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener auditoría por ID",
        description = "Obtiene los detalles de un registro de auditoría específico usando su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Auditoría no encontrada")
    })
    public ResponseEntity<Auditoria> obtenerAuditoria(
            @Parameter(description = "ID del registro de auditoría", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriaPorId(id));
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(
        summary = "Auditorías por usuario",
        description = "Obtiene todos los registros de auditoría asociados a un usuario específico"
    )
    @ApiResponse(responseCode = "200", description = "Lista de auditorías del usuario",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Auditoria>> obtenerAuditoriasPorUsuario(
            @Parameter(description = "Nombre del usuario", required = true)
            @PathVariable String usuario) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriasPorUsuario(usuario));
    }

    @GetMapping("/tipo/{tipoOperacion}")
    @Operation(
        summary = "Auditorías por tipo de operación",
        description = "Obtiene todos los registros de auditoría de un tipo de operación específico (CREAR, ACTUALIZAR, ELIMINAR, CONSULTAR)"
    )
    @ApiResponse(responseCode = "200", description = "Lista de auditorías por tipo de operación",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Auditoria>> obtenerAuditoriasPorTipo(
            @Parameter(description = "Tipo de operación", required = true, example = "CREAR")
            @PathVariable TipoOperacion tipoOperacion) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriasPorTipo(tipoOperacion));
    }
}
