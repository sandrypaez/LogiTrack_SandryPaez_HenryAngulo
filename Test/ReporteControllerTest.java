package Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.repository.MovimientoRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteControllerTest {
    
    @Autowired
    private MovimientoRepository movimientoRepository;

    @GetMapping("/movimientos")
    public ResponseEntity<Map<String, Object>> getReporteMovimientos() {
        Map<String, Object> reporte = new HashMap<>();
        
        long total = movimientoRepository.count();
        long entradas = movimientoRepository.countByTipo("ENTRADA");
        long salidas = movimientoRepository.countByTipo("SALIDA");
        long transferencias = movimientoRepository.countByTipo("TRANSFERENCIA");
        
        Map<String, Long> porTipo = new HashMap<>();
        porTipo.put("ENTRADA", entradas);
        porTipo.put("SALIDA", salidas);
        porTipo.put("TRANSFERENCIA", transferencias);
        
        reporte.put("totalMovimientos", total);
        reporte.put("movimientosPorTipo", porTipo);
        
        return ResponseEntity.ok(reporte);
    }
}