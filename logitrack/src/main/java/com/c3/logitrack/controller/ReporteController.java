package com.c3.logitrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.model.ReporteResumenDTO;
import com.c3.logitrack.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Generación de reportes y análisis del sistema")
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/resumen")
    @Operation(
        summary = "Resumen general del sistema",
        description = "Genera un resumen con información general incluyendo stock total por bodega y productos más movidos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte")
    })
    public ResponseEntity<ReporteResumenDTO> obtenerResumen() {
        ReporteResumenDTO resumen = reporteService.obtenerResumen();
        return ResponseEntity.ok(resumen);
    }
}
