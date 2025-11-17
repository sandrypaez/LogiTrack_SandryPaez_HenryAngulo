package com.c3.logitrack.config;

import com.c3.logitrack.entities.Auditoria;
import com.c3.logitrack.entities.enums.TipoOperacion;
import com.c3.logitrack.repository.AuditoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuditoriaListener {

    private static AuditoriaRepository auditoriaRepository;
    private static final ThreadLocal<Map<String, Object>> previousState = new ThreadLocal<>();

    @Autowired
    public void setAuditoriaRepository(AuditoriaRepository auditoriaRepository) {
        AuditoriaListener.auditoriaRepository = auditoriaRepository;
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(entity);
            Map<String, Object> state = new HashMap<>();
            state.put("entity", json);
            previousState.set(state);
        } catch (Exception e) {
            // Si falla la serialización, continuar sin valores anteriores
        }
    }

    @PostPersist
    public void postPersist(Object entity) {
        crearAuditoria(entity, TipoOperacion.INSERT, null, obtenerValoresNuevos(entity));
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        Map<String, Object> previous = previousState.get();
        String valoresAnteriores = previous != null ? (String) previous.get("entity") : null;
        crearAuditoria(entity, TipoOperacion.UPDATE, valoresAnteriores, obtenerValoresNuevos(entity));
        previousState.remove();
    }

    @PostRemove
    public void postRemove(Object entity) {
        crearAuditoria(entity, TipoOperacion.DELETE, obtenerValoresNuevos(entity), null);
    }

    private void crearAuditoria(Object entity, TipoOperacion tipoOperacion, String valoresAnteriores, String valoresNuevos) {
        if (auditoriaRepository == null) {
            return; // No hacer nada si el repositorio no está disponible
        }

        try {
            String username = obtenerUsernameActual();
            String entidadAfectada = entity.getClass().getSimpleName();
            Long entidadId = obtenerIdEntidad(entity);

            Auditoria auditoria = new Auditoria();
            auditoria.setTipoOperacion(tipoOperacion);
            auditoria.setUsuario(username != null ? username : "SISTEMA");
            auditoria.setEntidadAfectada(entidadAfectada);
            auditoria.setEntidadId(entidadId);
            auditoria.setValoresAnteriores(valoresAnteriores);
            auditoria.setValoresNuevos(valoresNuevos);

            auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            // Log del error pero no interrumpir la operación principal
            System.err.println("Error al crear auditoría: " + e.getMessage());
        }
    }

    private String obtenerUsernameActual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                return authentication.getName();
            }
        } catch (Exception e) {
            // Si no hay contexto de seguridad, continuar
        }
        return "SISTEMA";
    }

    private Long obtenerIdEntidad(Object entity) {
        try {
            java.lang.reflect.Method getId = entity.getClass().getMethod("getId");
            Object id = getId.invoke(entity);
            return id != null ? Long.valueOf(id.toString()) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String obtenerValoresNuevos(Object entity) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(entity);
        } catch (Exception e) {
            return entity.toString();
        }
    }
}

