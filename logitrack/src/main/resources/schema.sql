
CREATE DATABASE logitrack;

USE logitrack;


CREATE TABLE bodegas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    capacidad INT NOT NULL,
    encargado VARCHAR(100) NOT NULL
);

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    stock INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL
);

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    rol ENUM('ADMIN', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO'
);

CREATE TABLE movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    usuario_id BIGINT NOT NULL,
    bodega_origen_id BIGINT,
    bodega_destino_id BIGINT,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (bodega_origen_id) REFERENCES bodegas(id),
    FOREIGN KEY (bodega_destino_id) REFERENCES bodegas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE auditorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    fecha_hora DATETIME NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    entidad_afectada VARCHAR(100) NOT NULL,
    entidad_id BIGINT,
    valores_anteriores TEXT,
    valores_nuevos TEXT
);
