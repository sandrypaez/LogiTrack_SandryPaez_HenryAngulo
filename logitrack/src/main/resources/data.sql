


INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado) 
VALUES
    ('Bodega Central', 'Ciudad Principal', 5000, 'Juan Pérez'),
    ('Bodega Norte', 'Ciudad Norte', 3000, 'Ana González'),
    ('Bodega Sur', 'Ciudad Sur', 4000, 'Carlos Martínez');


INSERT INTO productos (nombre, categoria, stock, precio)
VALUES
    ('Producto A', 'Categoría 1', 100, 10.00),
    ('Producto B', 'Categoría 2', 200, 15.50),
    ('Producto C', 'Categoría 1', 50, 8.00),
    ('Producto D', 'Categoría 3', 30, 20.00);

-- Nota: Las contraseñas deben estar encriptadas con BCrypt en producción
-- Estas son contraseñas de ejemplo (1234 y abcd) que se encriptarán al ejecutar
INSERT INTO usuarios (username, password, email, rol) 
VALUES
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@logitrack.com', 'ADMIN'),
    ('empleado1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HFj9jO7dP.5L8K8Y5Y5Y5', 'empleado1@logitrack.com', 'EMPLEADO');


INSERT INTO movimientos (fecha, tipo_movimiento, usuario_id, bodega_origen_id, bodega_destino_id, producto_id, cantidad)
VALUES
    ('2025-11-01 10:00:00', 'ENTRADA', 1, NULL, 1, 1, 100),
    ('2025-11-02 11:00:00', 'SALIDA', 2, 1, NULL, 1, 50),
    ('2025-11-03 12:00:00', 'TRANSFERENCIA', 1, 1, 2, 2, 20);


INSERT INTO auditorias (tipo_operacion, fecha_hora, usuario, entidad_afectada, entidad_id, valores_anteriores, valores_nuevos)
VALUES
    ('INSERT', '2025-11-01 10:00:00', 'admin', 'Bodega', 1, NULL, '{"nombre": "Bodega Central", "ubicacion": "Ciudad Principal"}'),
    ('UPDATE', '2025-11-02 11:00:00', 'empleado1', 'Producto', 1, '{"stock": 100}', '{"stock": 50}'),
    ('DELETE', '2025-11-03 12:00:00', 'admin', 'Movimiento', 3, '{"cantidad": 20}', NULL);
