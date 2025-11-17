package com.c3.logitrack.controller;

import com.c3.logitrack.entities.Auditoria;
import com.c3.logitrack.entities.enums.TipoOperacion;
import com.c3.logitrack.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@CrossOrigin(origins = "*")
@Tag(name = "Auditoría", description = "Endpoints para consultar registros de auditoría")
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las auditorías", description = "Obtiene una lista de todos los registros de auditoría")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Auditoria>> listarAuditorias() {
        return ResponseEntity.ok(auditoriaService.listarAuditorias());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener auditoría por ID", description = "Obtiene un registro de auditoría específico por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Auditoria> obtenerAuditoria(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriaPorId(id));
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(summary = "Auditorías por usuario", description = "Obtiene todos los registros de auditoría de un usuario específico")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Auditoria>> obtenerAuditoriasPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriasPorUsuario(usuario));
    }

    @GetMapping("/tipo/{tipoOperacion}")
    @Operation(summary = "Auditorías por tipo de operación", description = "Obtiene todos los registros de auditoría de un tipo de operación específico")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Auditoria>> obtenerAuditoriasPorTipo(@PathVariable TipoOperacion tipoOperacion) {
        return ResponseEntity.ok(auditoriaService.obtenerAuditoriasPorTipo(tipoOperacion));
    }
}
