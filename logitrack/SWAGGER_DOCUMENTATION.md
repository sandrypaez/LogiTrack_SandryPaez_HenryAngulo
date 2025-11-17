# 📚 Documentación Swagger - LogiTrack API

## Acceso a la documentación

La documentación interactiva de la API está disponible en:

### Desarrollo
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## 🔐 Autenticación

Todos los endpoints protegidos requieren autenticación mediante **JWT (JSON Web Token)**.

### Pasos para autenticarse:

1. **Ir al endpoint `/auth/login`** en Swagger UI
2. **Ejecutar la solicitud** con credenciales:
   ```json
   {
     "username": "admin",
     "password": "admin"
   }
   ```
3. **Copiar el token JWT** de la respuesta
4. **Hacer clic en el botón "Authorize"** (candado) en Swagger UI
5. **Pegar el token** en el campo con el formato:
   ```
   Bearer <token-jwt>
   ```

## 📋 Estructura de la API

### 1. **Autenticación** 🔐
- `POST /auth/login` - Iniciar sesión
- `POST /auth/register` - Registrar nuevo usuario

### 2. **Productos** 📦
- `GET /productos` - Listar todos los productos
- `GET /productos/{id}` - Obtener producto por ID
- `GET /productos/stock-bajo` - Productos con stock bajo
- `POST /productos` - Crear nuevo producto (requiere ADMIN)
- `PUT /productos/{id}` - Actualizar producto (requiere ADMIN)
- `DELETE /productos/{id}` - Eliminar producto (requiere ADMIN)

### 3. **Bodegas** 🏭
- `GET /bodegas` - Listar todas las bodegas
- `GET /bodegas/{id}` - Obtener bodega por ID
- `POST /bodegas` - Crear nueva bodega (requiere ADMIN)
- `PUT /bodegas/{id}` - Actualizar bodega (requiere ADMIN)
- `DELETE /bodegas/{id}` - Eliminar bodega (requiere ADMIN)

### 4. **Movimientos** 🔄
- `GET /movimientos` - Listar todos los movimientos
- `GET /movimientos/{id}` - Obtener movimiento por ID
- `GET /movimientos/fechas?fechaInicio=...&fechaFin=...` - Movimientos por rango de fechas
- `POST /movimientos/entrada` - Registrar entrada (requiere ADMIN o EMPLEADO)
- `POST /movimientos/salida` - Registrar salida (requiere ADMIN o EMPLEADO)
- `POST /movimientos/transferencia` - Registrar transferencia (requiere ADMIN o EMPLEADO)
- `DELETE /movimientos/{id}` - Eliminar movimiento (requiere ADMIN)

### 5. **Auditoría** 📝
- `GET /auditorias` - Listar todas las auditorías
- `GET /auditorias/{id}` - Obtener auditoría por ID
- `GET /auditorias/usuario/{usuario}` - Auditorías por usuario
- `GET /auditorias/tipo/{tipoOperacion}` - Auditorías por tipo de operación

### 6. **Reportes** 📊
- `GET /reportes/resumen` - Resumen general del sistema

### 7. **Usuarios** 👥
- `GET /usuarios` - Listar todos los usuarios
- `GET /usuarios/{id}` - Obtener usuario por ID
- `GET /usuarios/username/{username}` - Obtener usuario por username

## 🔑 Roles y Permisos

### ADMIN
- Acceso completo a todos los endpoints
- Puede crear, actualizar y eliminar recursos
- Puede ver y gestionar auditorías

### EMPLEADO
- Puede registrar movimientos (entrada, salida, transferencia)
- Puede consultar productos, bodegas y movimientos
- Puede consultar auditorías

## 📝 Ejemplos de Uso

### Ejemplo 1: Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

**Respuesta:**
```json
{
  "message": "Inicio de sesión exitoso",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "usuario": "admin",
  "email": "admin@logitrack.com",
  "rol": "ADMIN"
}
```

### Ejemplo 2: Crear Producto (requiere autenticación)
```bash
curl -X POST http://localhost:8080/productos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token-jwt>" \
  -d '{
    "nombre": "Laptop Dell",
    "descripcion": "Laptop de negocios",
    "precio": 999.99,
    "cantidad": 50
  }'
```

### Ejemplo 3: Listar Movimientos por Rango de Fechas
```bash
curl -X GET "http://localhost:8080/movimientos/fechas?fechaInicio=2025-01-01T00:00:00&fechaFin=2025-12-31T23:59:59" \
  -H "Authorization: Bearer <token-jwt>"
```

## 🛠️ Configuración

### Archivo: `SwaggerConfig.java`
La configuración de Swagger se encuentra en `src/main/java/com/c3/logitrack/config/SwaggerConfig.java`

**Características:**
- Información general de la API
- Esquema de autenticación JWT
- Servidores (desarrollo y producción)
- Información de contacto

## ⚙️ Dependencias

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

## 📖 Especificación OpenAPI

La especificación OpenAPI 3.0 está disponible en:
- JSON: http://localhost:8080/v3/api-docs
- YAML: http://localhost:8080/v3/api-docs.yaml

## 🐛 Solución de Problemas

### No puedo acceder a Swagger UI
- Verifica que el servidor esté corriendo en http://localhost:8080
- Asegúrate de que el puerto 8080 no esté en uso por otra aplicación
- Comprueba los logs de la aplicación

### Recibo error 401 (No Autorizado)
- Necesitas estar autenticado para acceder a endpoints protegidos
- Obtén un token JWT en el endpoint `/auth/login`
- Asegúrate de incluir el token en el header `Authorization: Bearer <token>`

### El token JWT expiró
- Vuelve a hacer login para obtener un nuevo token
- Utiliza el nuevo token en las solicitudes posteriores

## 📞 Soporte

Para más información o reportar problemas, contacta al equipo de soporte:
- Email: soporte@logitrack.com
- Sitio web: https://www.logitrack.com
