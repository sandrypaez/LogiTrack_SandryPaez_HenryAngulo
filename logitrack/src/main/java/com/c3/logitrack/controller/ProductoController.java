package com.c3.logitrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.logitrack.entities.Producto;
import com.c3.logitrack.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestión de productos del inventario")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene una lista completa de todos los productos en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener producto por ID",
        description = "Obtiene los detalles de un producto específico usando su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> obtenerProducto(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/stock-bajo")
    @Operation(
        summary = "Productos con stock bajo",
        description = "Obtiene una lista de productos con stock inferior a 10 unidades"
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos con stock bajo",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Producto>> obtenerProductosStockBajo() {
        List<Producto> productos = productoService.obtenerProductosStockBajo();
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo producto en el sistema (requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crearProducto(
            @Parameter(description = "Datos del producto a crear")
            @Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crearProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza los datos de un producto existente (requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del producto")
            @Valid @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del sistema (requiere rol ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - requiere rol ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
