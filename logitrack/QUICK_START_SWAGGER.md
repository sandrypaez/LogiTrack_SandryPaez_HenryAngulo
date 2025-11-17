# 🚀 Guía de Inicio Rápido - Documentación Swagger

## ¿Qué se ha mejorado?

Se ha completado y mejorado la documentación Swagger de la API REST LogiTrack con:

✅ **Configuración mejorada de Swagger** con información detallada de la API  
✅ **Documentación completa de todos los endpoints** con descripciones claras  
✅ **Anotaciones detalladas** en todos los controladores  
✅ **Ejemplos de respuesta** en la documentación  
✅ **Información de autenticación** y autorización  
✅ **Documentación de modelos** y estructuras de datos  
✅ **Códigos HTTP** y mensajes de error documentados  

## 📍 Acceso a la Documentación

### Opción 1: Desde el navegador (Recomendado)
Una vez que la aplicación esté corriendo:

1. Abre tu navegador
2. Ve a: **http://localhost:8080/swagger-ui.html**
3. ¡Listo! Ya verás la documentación interactiva

### Opción 2: Especificación OpenAPI
- **JSON**: http://localhost:8080/v3/api-docs
- **YAML**: http://localhost:8080/v3/api-docs.yaml

## 🔑 Primeros Pasos

### 1️⃣ Iniciar el servidor
```bash
cd c:\Users\carol\OneDrive\Desktop\LogiTrack_SandryPaez_HenryAngulo\logitrack
.\mvnw.cmd spring-boot:run
```

### 2️⃣ Acceder a Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 3️⃣ Obtener token de autenticación
1. En Swagger UI, expande **"Autenticación"**
2. Haz clic en **POST /auth/login**
3. Haz clic en **"Try it out"**
4. Ingresa las credenciales por defecto:
   ```json
   {
     "username": "admin",
     "password": "admin"
   }
   ```
5. Haz clic en **"Execute"**
6. Copia el token JWT de la respuesta

### 4️⃣ Autorizar en Swagger UI
1. Haz clic en el botón **"Authorize"** (candado verde) en la esquina superior derecha
2. En el campo de entrada, pega: `Bearer <tu-token-jwt>`
3. Haz clic en **"Authorize"**
4. ¡Ahora puedes ejecutar todos los endpoints protegidos!

## 📚 Documentos Generados

Se han creado dos documentos de referencia:

### 1. **SWAGGER_DOCUMENTATION.md**
- Acceso a la documentación
- Guía de autenticación
- Estructura completa de endpoints
- Ejemplos de uso con curl
- Solución de problemas

### 2. **API_MODELS.md**
- Estructura de todas las entidades
- DTOs (Data Transfer Objects)
- Enumeraciones
- Códigos de estado HTTP
- Validaciones de campos

## 🧪 Prueba Rápida

### Endpoint 1: Registrar Usuario (sin autenticación)
```
POST /auth/register
{
  "username": "empleado_test",
  "email": "test@logitrack.com",
  "password": "Test1234",
  "rol": "EMPLEADO"
}
```

### Endpoint 2: Login (sin autenticación)
```
POST /auth/login
{
  "username": "admin",
  "password": "admin"
}
```

### Endpoint 3: Listar Productos (requiere token)
```
GET /productos
Header: Authorization: Bearer <token>
```

## 📋 Estructura de Endpoints Principales

### Autenticación 🔐
- `POST /auth/login` - Iniciar sesión
- `POST /auth/register` - Registrar usuario

### Gestión de Inventario 📦
- `GET /productos` - Listar productos
- `GET /bodegas` - Listar bodegas
- `GET /movimientos` - Listar movimientos

### Reportes 📊
- `GET /reportes/resumen` - Resumen general

### Auditoría 📝
- `GET /auditorias` - Registros de auditoría

## 🔒 Autenticación JWT

Todos los endpoints (excepto `/auth/login` y `/auth/register`) requieren un token JWT.

**Formato del Header:**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## 🛠️ Dependencias Instaladas

Se está utilizando:
- **springdoc-openapi-starter-webmvc-ui:2.1.0** - Swagger/OpenAPI UI
- **Spring Security** - Autenticación
- **JWT** - Tokens de autenticación

## 📖 Más Información

Consulta los archivos de documentación creados:
- `SWAGGER_DOCUMENTATION.md` - Documentación completa
- `API_MODELS.md` - Modelos de datos

## ⚡ Troubleshooting

**¿No puedo acceder a Swagger UI?**
- Verifica que el servidor esté corriendo: `http://localhost:8080`
- Revisa los logs de la aplicación
- Asegúrate de que el puerto 8080 esté disponible

**¿Me da error 401 No Autorizado?**
- Necesitas obtener un token en `/auth/login` primero
- Copia el token en el header `Authorization: Bearer <token>`
- Usa el botón "Authorize" en Swagger UI

**¿El token ha expirado?**
- Vuelve a hacer login para obtener un nuevo token
- Actualiza la autorización en Swagger UI

## 📞 Contacto

Soporte: soporte@logitrack.com

---

**¡La documentación Swagger está lista para usar!** 🎉
