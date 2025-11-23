package Test;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.model.MovimientoDTO;
import com.c3.logitrack.repository.MovimientoRepository;

@RestController
@RequestMapping("/movimientos")
public class MovimientoControllerTest { 

    @Autowired
    private MovimientoRepository movimientoRepository;

    @GetMapping("/movimientos/recientes")
    public ResponseEntity<List<MovimientoDTO>> listarRecientes() {
        List<Movimiento> movimientos = movimientoRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "fecha"))
        ).getContent();
        
        List<MovimientoDTO> dtos = movimientos.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    private MovimientoDTO convertirADTO(Movimiento m) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(m.getId());
        dto.setTipo(m.getTipo());
        dto.setCantidad(m.getCantidad());
        dto.setFecha(m.getFecha());
        return dto;
    }
}