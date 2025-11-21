package Test;

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
public class MovimientoControllerTest { 

    @GetMapping("/moviemientos/recientes")
    @Operation(summary = "listar los ultimos 10 movimientos registrados")
    public ResponseEntity<List<MovimientoDTO>> ListarRecientes()
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin, 
    return ResponseEntity.ok(movimientoService.obtenerMovimientosPorFechas(fechaInicio, fechaFin));
}