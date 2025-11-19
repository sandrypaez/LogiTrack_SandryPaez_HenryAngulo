package com.c3.logitrack.model;

import java.util.Date;

public class MovimientoRequest {
    private Long productoId;
    private Long bodegaOrigenId;
    private Long bodegaDestinoId;
    private String tipoMovimiento;
    private int cantidad;
    private Date fecha;

    // Getters y setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Long getBodegaOrigenId() { return bodegaOrigenId; }
    public void setBodegaOrigenId(Long bodegaOrigenId) { this.bodegaOrigenId = bodegaOrigenId; }

    public Long getBodegaDestinoId() { return bodegaDestinoId; }
    public void setBodegaDestinoId(Long bodegaDestinoId) { this.bodegaDestinoId = bodegaDestinoId; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
