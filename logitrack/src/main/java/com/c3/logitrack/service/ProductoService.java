package com.c3.logitrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c3.logitrack.entities.Producto;
import com.c3.logitrack.exception.BadRequestException;
import com.c3.logitrack.exception.ResourceNotFoundException;
import com.c3.logitrack.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        if (id == null) {
            throw new BadRequestException("El id del producto es obligatorio");
        }
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

    @SuppressWarnings("null")
    public void eliminarProducto(Long id) {
        if (id == null) {
            throw new BadRequestException("El id del producto es obligatorio");
        }
        Producto producto = obtenerProductoPorId(id);
        productoRepository.delete(producto);
    }
}