package com.c3.logitrack.service;

import com.c3.logitrack.entities.Auditoria;
import com.c3.logitrack.entities.enums.TipoOperacion;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public List<Auditoria> listarAuditorias() {
        return auditoriaRepository.findAll();
    }

    public Auditoria obtenerAuditoriaPorId(Long id) {
        return auditoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de auditoría no encontrado con id: " + id));
    }

    public List<Auditoria> obtenerAuditoriasPorUsuario(String usuario) {
        return auditoriaRepository.findByUsuario(usuario);
    }

    public List<Auditoria> obtenerAuditoriasPorTipo(TipoOperacion tipoOperacion) {
        return auditoriaRepository.findByTipoOperacion(tipoOperacion);
    }
}
