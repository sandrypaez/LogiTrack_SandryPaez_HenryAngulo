package com.c3.logitrack.service;

import com.c3.logitrack.entities.Bodega;
import com.c3.logitrack.repository.BodegaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BodegaService {

    private final BodegaRepository bodegaRepository;

    public BodegaService(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    // Obtener todas las bodegas
    public List<Bodega> listarBodegas() {
        return bodegaRepository.findAll();
    }

    // Buscar bodega por ID
    @SuppressWarnings("null")
    public Bodega obtenerBodegaPorId(Long id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bodega con ID " + id + " no encontrada"));
    }

    // Buscar bodega por nombre
    public Bodega obtenerBodegaPorNombre(String nombre) {
        return bodegaRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Bodega con nombre '" + nombre + "' no encontrada"));
    }

    // Crear nueva bodega
    public Bodega crearBodega(Bodega bodega) {
        // Validar que no exista otra bodega con el mismo nombre
        Optional<Bodega> existente = bodegaRepository.findByNombre(bodega.getNombre());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una bodega con el nombre: " + bodega.getNombre());
        }
        return bodegaRepository.save(bodega);
    }

    // Actualizar bodega
    public Bodega actualizarBodega(Long id, Bodega bodegaActualizada) {
        Bodega bodegaExistente = obtenerBodegaPorId(id);

        // Validar duplicados al cambiar nombre
        if (!bodegaExistente.getNombre().equals(bodegaActualizada.getNombre())) {
            Optional<Bodega> otra = bodegaRepository.findByNombre(bodegaActualizada.getNombre());
            if (otra.isPresent()) {
                throw new IllegalArgumentException("Ya existe otra bodega con el nombre: " + bodegaActualizada.getNombre());
            }
        }

        bodegaExistente.setNombre(bodegaActualizada.getNombre());
        bodegaExistente.setUbicacion(bodegaActualizada.getUbicacion());
        bodegaExistente.setCapacidad(bodegaActualizada.getCapacidad());
        bodegaExistente.setEncargado(bodegaActualizada.getEncargado());

        return bodegaRepository.save(bodegaExistente);
    }

    // Eliminar bodega
    @SuppressWarnings("null")
    public void eliminarBodega(Long id) {
        Bodega bodegaExistente = obtenerBodegaPorId(id);
        bodegaRepository.delete(bodegaExistente);
    }
}
