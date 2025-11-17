package com.c3.logitrack.repository;

import com.c3.logitrack.entities.Movimiento;
import com.c3.logitrack.entities.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByTipoMovimiento(TipoMovimiento tipoMovimiento);

    @Query("SELECT m FROM Movimiento m WHERE m.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Movimiento> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                        @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT m FROM Movimiento m WHERE m.fecha BETWEEN :fechaInicio AND :fechaFin AND m.tipoMovimiento = :tipo")
    List<Movimiento> findByFechaBetweenAndTipoMovimiento(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                          @Param("fechaFin") LocalDateTime fechaFin,
                                                          @Param("tipo") TipoMovimiento tipo);
}
