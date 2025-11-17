# 🗂️ Modelos de Datos - LogiTrack API

## Entidades Principales

### Usuario
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@logitrack.com",
  "rol": "ADMIN",
  "activo": true,
  "fechaCreacion": "2025-01-01T10:00:00"
}
```

**Campos:**
- `id` (Long): Identificador único
- `username` (String): Nombre de usuario único
- `email` (String): Correo electrónico
- `rol` (Enum): ADMIN, EMPLEADO
- `activo` (Boolean): Estado del usuario
- `fechaCreacion` (LocalDateTime): Fecha de creación

---

### Producto
```json
{
  "id": 1,
  "nombre": "Laptop Dell XPS 13",
  "descripcion": "Laptop ultradelgada de alto rendimiento",
  "precio": 1299.99,
  "cantidad": 45,
  "bodega": {
    "id": 1,
    "nombre": "Bodega Principal"
  },
  "fechaCreacion": "2025-01-15T14:30:00",
  "fechaActualizacion": "2025-11-16T10:20:00"
}
```

**Campos:**
- `id` (Long): Identificador único
- `nombre` (String): Nombre del producto
- `descripcion` (String): Descripción detallada
- `precio` (BigDecimal): Precio unitario
- `cantidad` (Integer): Stock disponible
- `bodega` (Bodega): Bodega donde se almacena
- `fechaCreacion` (LocalDateTime): Fecha de creación
- `fechaActualizacion` (LocalDateTime): Última actualización

---

### Bodega
```json
{
  "id": 1,
  "nombre": "Bodega Principal",
  "ubicacion": "Centro, Calle Principal 123",
  "capacidad": 1000,
  "encargado": "Carlos López",
  "telefono": "+1-555-0100",
  "email": "bodega.principal@logitrack.com",
  "activa": true,
  "fechaCreacion": "2025-01-01T09:00:00"
}
```

**Campos:**
- `id` (Long): Identificador único
- `nombre` (String): Nombre de la bodega
- `ubicacion` (String): Ubicación geográfica
- `capacidad` (Integer): Capacidad máxima
- `encargado` (String): Responsable de la bodega
- `telefono` (String): Teléfono de contacto
- `email` (String): Email de contacto
- `activa` (Boolean): Estado operacional
- `fechaCreacion` (LocalDateTime): Fecha de creación

---

### Movimiento
```json
{
  "id": 1,
  "tipo": "ENTRADA",
  "producto": {
    "id": 1,
    "nombre": "Laptop Dell XPS 13"
  },
  "bodegaOrigen": {
    "id": 1,
    "nombre": "Bodega Principal"
  },
  "bodegaDestino": null,
  "cantidad": 10,
  "descripcion": "Compra a proveedor TechSupply",
  "usuario": "admin",
  "fechaMovimiento": "2025-11-16T15:45:00",
  "referencia": "COMP-2025-001"
}
```

**Campos:**
- `id` (Long): Identificador único
- `tipo` (Enum): ENTRADA, SALIDA, TRANSFERENCIA
- `producto` (Producto): Producto involucrado
- `bodegaOrigen` (Bodega): Bodega de origen
- `bodegaDestino` (Bodega): Bodega de destino (solo transferencias)
- `cantidad` (Integer): Cantidad movida
- `descripcion` (String): Descripción del movimiento
- `usuario` (String): Usuario que realizó el movimiento
- `fechaMovimiento` (LocalDateTime): Fecha del movimiento
- `referencia` (String): Referencia externa (pedido, factura, etc.)

---

### Auditoría
```json
{
  "id": 1,
  "tipoOperacion": "CREAR",
  "entidad": "Producto",
  "idEntidad": 1,
  "usuario": "admin",
  "descripcion": "Producto 'Laptop Dell XPS 13' creado",
  "detallesAnteriores": null,
  "detallesNuevos": {
    "nombre": "Laptop Dell XPS 13",
    "precio": 1299.99,
    "cantidad": 45
  },
  "fechaOperacion": "2025-11-16T14:30:00",
  "ipCliente": "192.168.1.100"
}
```

**Campos:**
- `id` (Long): Identificador único
- `tipoOperacion` (Enum): CREAR, ACTUALIZAR, ELIMINAR, CONSULTAR
- `entidad` (String): Tipo de entidad modificada
- `idEntidad` (Long): ID de la entidad
- `usuario` (String): Usuario que realizó la operación
- `descripcion` (String): Descripción de la operación
- `detallesAnteriores` (Map): Estado anterior (si aplica)
- `detallesNuevos` (Map): Estado nuevo
- `fechaOperacion` (LocalDateTime): Fecha de la operación
- `ipCliente` (String): IP del cliente

---

### Reporte Resumen
```json
{
  "totalProductos": 150,
  "totalBodegas": 3,
  "totalMovimientos": 2450,
  "valorTotalInventario": 45000.00,
  "stockPorBodega": [
    {
      "bodegaId": 1,
      "bodegaNombre": "Bodega Principal",
      "totalProductos": 75,
      "valorTotal": 25000.00
    },
    {
      "bodegaId": 2,
      "bodegaNombre": "Bodega Secundaria",
      "totalProductos": 50,
      "valorTotal": 15000.00
    },
    {
      "bodegaId": 3,
      "bodegaNombre": "Bodega Distribución",
      "totalProductos": 25,
      "valorTotal": 5000.00
    }
  ],
  "productosMasMovidos": [
    {
      "productoId": 1,
      "nombre": "Laptop Dell XPS 13",
      "movimientos": 450
    },
    {
      "productoId": 2,
      "nombre": "Mouse Logitech",
      "movimientos": 320
    },
    {
      "productoId": 3,
      "nombre": "Teclado Mecánico",
      "movimientos": 280
    }
  ]
}
```

**Campos:**
- `totalProductos` (Integer): Total de productos en inventario
- `totalBodegas` (Integer): Total de bodegas
- `totalMovimientos` (Integer): Total de movimientos registrados
- `valorTotalInventario` (BigDecimal): Valor total del inventario
- `stockPorBodega` (Array): Stock desglosado por bodega
- `productosMasMovidos` (Array): Top 3 productos más movidos

---

## DTOs (Data Transfer Objects)

### LoginRequest
```json
{
  "username": "admin",
  "password": "admin"
}
```

### RegisterRequest
```json
{
  "username": "empleado1",
  "email": "empleado1@logitrack.com",
  "password": "password123",
  "rol": "EMPLEADO"
}
```

### MovimientoRequest
```json
{
  "productoId": 1,
  "bodegaOrigenId": 1,
  "bodegaDestinoId": 2,
  "cantidad": 10,
  "descripcion": "Transferencia entre bodegas",
  "referencia": "REF-2025-001"
}
```

---

## Enumeraciones

### RolUsuario
- `ADMIN` - Administrador del sistema
- `EMPLEADO` - Empleado estándar

### TipoOperacion
- `CREAR` - Creación de recurso
- `ACTUALIZAR` - Actualización de recurso
- `ELIMINAR` - Eliminación de recurso
- `CONSULTAR` - Lectura de recurso

### TipoMovimiento
- `ENTRADA` - Entrada de productos
- `SALIDA` - Salida de productos
- `TRANSFERENCIA` - Transferencia entre bodegas

---

## Códigos de Estado HTTP

| Código | Significado | Descripción |
|--------|-------------|-------------|
| 200 | OK | Solicitud exitosa |
| 201 | Created | Recurso creado exitosamente |
| 204 | No Content | Solicitud exitosa, sin contenido |
| 400 | Bad Request | Datos inválidos |
| 401 | Unauthorized | Autenticación requerida |
| 403 | Forbidden | Acceso denegado |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error del servidor |

---

## Validaciones

### Producto
- `nombre`: Requerido, máximo 255 caracteres
- `precio`: Requerido, debe ser > 0
- `cantidad`: Requerido, debe ser >= 0
- `descripcion`: Opcional, máximo 1000 caracteres

### Bodega
- `nombre`: Requerido, máximo 255 caracteres
- `ubicacion`: Requerido, máximo 500 caracteres
- `capacidad`: Requerido, debe ser > 0
- `email`: Formato de email válido (opcional)

### Usuario
- `username`: Requerido, único, 3-50 caracteres
- `email`: Requerido, formato de email válido, único
- `password`: Requerido, mínimo 8 caracteres

### Movimiento
- `cantidad`: Requerido, debe ser > 0
- `producto`: Requerido
- `bodegaOrigen`: Requerido
- Para transferencias, `bodegaDestino` es requerido

