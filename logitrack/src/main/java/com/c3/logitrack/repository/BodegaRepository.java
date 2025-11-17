package com.c3.logitrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c3.logitrack.entities.Bodega;

import java.util.Optional;


public interface BodegaRepository extends JpaRepository <Bodega, Long>{
    Optional<Bodega> findByNombre(String nombre);
    
}
