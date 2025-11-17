package com.c3.logitrack.service;

import com.c3.logitrack.entities.Producto;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    public List<Producto> obtenerProductosStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    public Producto crearProducto(Producto producto) {
        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoDetails) {
        Producto producto = obtenerProductoPorId(id);
        producto.setNombre(productoDetails.getNombre());
        producto.setCategoria(productoDetails.getCategoria());
        producto.setStock(productoDetails.getStock());
        producto.setPrecio(productoDetails.getPrecio());
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerProductoPorId(id);
        productoRepository.delete(producto);
    }
}