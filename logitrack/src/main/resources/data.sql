
INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado) 
VALUES
    ('Bodega Central', 'Ciudad Principal', 5000, 'Juan Pérez'),
    ('Bodega Norte', 'Ciudad Norte', 3000, 'Ana González'),
    ('Bodega Sur', 'Ciudad Sur', 4000, 'Carlos Martínez'),
    ('Bodega Este', 'Ciudad Este', 3500, 'María López'),
    ('Bodega Oeste', 'Ciudad Oeste', 4500, 'Pedro Ramírez'),
    ('Bodega Centro', 'Ciudad Centro', 5500, 'Laura Torres'),
    ('Bodega Industrial', 'Zona Industrial', 6000, 'Roberto Díaz'),
    ('Bodega Portuaria', 'Puerto Principal', 4000, 'Sofia Herrera'),
    ('Bodega Rural', 'Campo Verde', 2500, 'Miguel Castro'),
    ('Bodega Urbana', 'Centro Urbano', 3200, 'Elena Vargas'),
    ('Bodega Logística', 'Parque Logístico', 7000, 'Andrés Morales'),
    ('Bodega Frigorífica', 'Zona Fría', 3800, 'Carmen Ruiz'),
    ('Bodega Aérea', 'Aeropuerto', 4200, 'Fernando Gómez'),
    ('Bodega Marítima', 'Costa Azul', 4800, 'Isabel Peña'),
    ('Bodega Suburbana', 'Suburbio Norte', 3600, 'Diego Silva');

INSERT INTO productos (nombre, categoria, stock, precio)
VALUES
    ('Producto A', 'Categoría 1', 100, 10.00),
    ('Producto B', 'Categoría 2', 200, 15.50),
    ('Producto C', 'Categoría 1', 50, 8.00),
    ('Producto D', 'Categoría 3', 30, 20.00),
    ('Producto E', 'Categoría 1', 120, 12.00),
    ('Producto F', 'Categoría 2', 180, 18.00),
    ('Producto G', 'Categoría 3', 40, 25.00),
    ('Producto H', 'Categoría 1', 90, 9.50),
    ('Producto I', 'Categoría 2', 150, 14.00),
    ('Producto J', 'Categoría 3', 60, 22.00),
    ('Producto K', 'Categoría 1', 110, 11.00),
    ('Producto L', 'Categoría 2', 170, 16.50),
    ('Producto M', 'Categoría 3', 35, 19.00),
    ('Producto N', 'Categoría 1', 80, 7.50),
    ('Producto O', 'Categoría 2', 140, 13.00);

INSERT INTO usuarios (username, password, email, rol) 
VALUES
    ('empleado2', 'efgh', 'empleado2@logitrack.com', 'EMPLEADO'),
    ('empleado3', 'ijkl', 'empleado3@logitrack.com', 'EMPLEADO'),
    ('admin2', '5678', 'admin2@logitrack.com', 'ADMIN'),
    ('empleado4', 'mnop', 'empleado4@logitrack.com', 'EMPLEADO'),
    ('empleado5', 'qrst', 'empleado5@logitrack.com', 'EMPLEADO'),
    ('admin3', '9012', 'admin3@logitrack.com', 'ADMIN'),
    ('empleado6', 'uvwx', 'empleado6@logitrack.com', 'EMPLEADO'),
    ('empleado7', 'yzab', 'empleado7@logitrack.com', 'EMPLEADO'),
    ('admin4', '3456', 'admin4@logitrack.com', 'ADMIN'),
    ('empleado8', 'cdef', 'empleado8@logitrack.com', 'EMPLEADO'),
    ('empleado9', 'ghij', 'empleado9@logitrack.com', 'EMPLEADO'),
    ('admin5', '7890', 'admin5@logitrack.com', 'ADMIN'),
    ('empleado10', 'klmn', 'empleado10@logitrack.com', 'EMPLEADO');
INSERT INTO movimientos (fecha, tipo_movimiento, usuario_id, bodega_origen_id, bodega_destino_id, producto_id, cantidad)
VALUES
    ('2025-11-04 13:00:00', 'ENTRADA', 1, NULL, 3, 3, 75),
    ('2025-11-05 14:00:00', 'SALIDA', 2, 2, NULL, 2, 30),
    ('2025-11-07 16:00:00', 'ENTRADA', 3, NULL, 4, 5, 90),
    ('2025-11-08 17:00:00', 'SALIDA', 4, 4, NULL, 3, 40),
    ('2025-11-09 18:00:00', 'TRANSFERENCIA', 5, 1, 5, 6, 35),
    ('2025-11-10 19:00:00', 'ENTRADA', 6, NULL, 2, 7, 110),
    ('2025-11-11 20:00:00', 'SALIDA', 7, 5, NULL, 4, 55),
    ('2025-11-12 21:00:00', 'TRANSFERENCIA', 8, 2, 3, 8, 45),
    ('2025-11-13 22:00:00', 'ENTRADA', 9, NULL, 1, 9, 80),
    ('2025-11-14 23:00:00', 'SALIDA', 10, 3, NULL, 5, 60),
    ('2025-11-15 00:00:00', 'TRANSFERENCIA', 11, 4, 2, 10, 50);


INSERT INTO auditorias (tipo_operacion, fecha_hora, usuario, entidad_afectada, entidad_id, valores_anteriores, valores_nuevos)
VALUES
    ('INSERT', '2025-11-04 13:00:00', 'admin2', 'Producto', 5, NULL, '{"nombre": "Producto E", "categoria": "Categoría 1"}'),
    ('UPDATE', '2025-11-05 14:00:00', 'empleado2', 'Bodega', 2, '{"capacidad": 3000}', '{"capacidad": 3200}'),
    ('DELETE', '2025-11-06 15:00:00', 'admin', 'Usuario', 3, '{"username": "empleado2"}', NULL),
    ('INSERT', '2025-11-07 16:00:00', 'empleado3', 'Movimiento', 4, NULL, '{"tipo_movimiento": "ENTRADA", "cantidad": 75}'),
    ('UPDATE', '2025-11-08 17:00:00', 'admin3', 'Producto', 3, '{"precio": 8.00}', '{"precio": 9.00}'),
    ('DELETE', '2025-11-09 18:00:00', 'empleado4', 'Auditoria', 4, '{"tipo_operacion": "INSERT"}', NULL),
    ('INSERT', '2025-11-10 19:00:00', 'admin4', 'Bodega', 6, NULL, '{"nombre": "Bodega Centro", "ubicacion": "Ciudad Centro"}'),
    ('UPDATE', '2025-11-11 20:00:00', 'empleado5', 'Usuario', 5, '{"rol": "ADMIN"}', '{"rol": "EMPLEADO"}'),
    ('DELETE', '2025-11-12 21:00:00', 'admin5', 'Producto', 7, '{"stock": 40}', NULL),
    ('INSERT', '2025-11-13 22:00:00', 'empleado6', 'Movimiento', 7, NULL, '{"tipo_movimiento": "SALIDA", "cantidad": 40}'),
    ('UPDATE', '2025-11-14 23:00:00', 'admin2', 'Bodega', 4, '{"encargado": "María López"}', '{"encargado": "Ana López"}'),
    ('DELETE', '2025-11-15 00:00:00', 'empleado7', 'Auditoria', 7, '{"fecha_hora": "2025-11-07 16:00:00"}', NULL);



