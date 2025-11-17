package com.c3.logitrack.repository;

import com.c3.logitrack.entities.Auditoria;
import com.c3.logitrack.entities.enums.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
   
   @SuppressWarnings("null")
   Optional<Auditoria> findById(Long id);
   
   List<Auditoria> findByUsuario(String usuario);
   
   List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);
}