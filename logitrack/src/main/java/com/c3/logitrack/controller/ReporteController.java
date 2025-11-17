package com.c3.logitrack.controller;

import com.c3.logitrack.model.ReporteResumenDTO;
import com.c3.logitrack.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Endpoints para generar reportes del sistema")
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/resumen")
    @Operation(summary = "Resumen general", description = "Obtiene un resumen general con stock total por bodega y productos más movidos")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente")
    })
    public ResponseEntity<ReporteResumenDTO> obtenerResumen() {
        ReporteResumenDTO resumen = reporteService.obtenerResumen();
        return ResponseEntity.ok(resumen);
    }
}

