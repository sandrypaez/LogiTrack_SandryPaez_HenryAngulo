package com.c3.logitrack.repository;

import com.c3.logitrack.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByNombre(String nombre);
    
    @Query("SELECT p FROM Producto p WHERE p.stock < 10")
    List<Producto> findProductosConStockBajo();
    
    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo")
    List<Producto> findProductosConStockMenorA(Integer stockMinimo);
}