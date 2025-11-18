# LogiTrack - Sistema de Gestión y Auditoría de Inventarios

Una aplicación Spring Boot completa para gestión de bodegas, productos, movimientos de inventario y auditoría. Incluye autenticación JWT, seguridad configurada y documentación OpenAPI/Swagger.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecución](#ejecución)
- [Perfiles de Aplicación](#perfiles-de-aplicación)
- [Documentación API](#documentación-api)
- [Endpoints](#endpoints)
- [Autenticación](#autenticación)
- [Base de Datos](#base-de-datos)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Dependencias](#dependencias)
- [Notas Importantes](#notas-importantes)

---

## ✨ Características

- **Gestión de Inventarios**: Administración completa de bodegas, productos y movimientos.
- **Auditoría Automática**: Registro automático de todas las operaciones (INSERT, UPDATE, DELETE).
- **Autenticación JWT**: Sistema seguro de autenticación basado en tokens JWT.
- **Control de Acceso Basado en Roles**: Roles ADMIN y EMPLEADO con permisos diferenciados.
- **Documentación Swagger/OpenAPI**: Especificación completa de la API REST con UI interactiva.
- **Validación de Datos**: Validaciones usando Bean Validation (Hibernate Validator).
- **Manejo Global de Excepciones**: Respuestas consistentes y estructuradas para errores.
- **CORS Configurado**: Soporte para peticiones desde diferentes orígenes.
- **Perfiles de Aplicación**: Perfiles `dev` y `prod` para distintos entornos.

---

## 🔧 Requisitos Previos

- **Java 17+** (Proyecto usa Java 17)
- **Maven 3.6+** (o usa el wrapper `mvnw` incluido)
- **MySQL 8.0+** (para la base de datos)
- **Git** (opcional, para clonar el repositorio)

### Verificar Java

```powershell
java -version
```

Debe mostrar algo como:
```
java version "17.0.x"
```

---

## 📦 Instalación y Configuración

### 1. Clonar el Repositorio (si aplica)

```powershell
git clone https://github.com/sandrypaez/LogiTrack_SandryPaez_HenryAngulo.git
cd LogiTrack_SandryPaez_HenryAngulo/logitrack
```

### 2. Configurar la Base de Datos

#### Crear la base de datos en MySQL:

```sql
CREATE DATABASE logitrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Usuario y permisos:

```sql
CREATE USER 'campus2023'@'localhost' IDENTIFIED BY 'campus2023';
GRANT ALL PRIVILEGES ON logitrack.* TO 'campus2023'@'localhost';
FLUSH PRIVILEGES;
```

> Nota: Las credenciales por defecto en `application.properties` son `campus2023/campus2023`. Cámbilas en producción.

### 3. Configuración de la Aplicación

Las propiedades de la aplicación están en `src/main/resources/application.properties`:

```properties
# Servidor
server.port=8080

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=campus2023
spring.datasource.password=campus2023

# JWT
jwt.secret=LogiTrackSecretKeyForJWTTokenGeneration2024SecureKeyMustBeAtLeast32Characters
jwt.expiration=86400000  # 24 horas en milisegundos

# Swagger/OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
```

#### Cambiar credenciales (IMPORTANTE en producción):

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
jwt.secret=tu_clave_secreta_de_32_caracteres_o_mas
```

---

## 🚀 Ejecución

### Opción 1: Maven (Recomendado)

En PowerShell, desde el directorio `logitrack`:

```powershell
# Build sin tests
.\mvnw.cmd -DskipTests package

# Ejecutar
.\mvnw.cmd spring-boot:run
```

O en una sola línea:

```powershell
.\mvnw.cmd -DskipTests clean install; .\mvnw.cmd spring-boot:run
```

### Opción 2: Ejecutar el JAR

```powershell
# Build
.\mvnw.cmd clean package -DskipTests

# Ejecutar
java -jar target/logitrack-0.0.1-SNAPSHOT.jar
```

### Opción 3: Desde tu IDE (IntelliJ, Eclipse, VS Code)

1. Abre el proyecto en tu IDE.
2. Busca la clase `LogitrackApplication` (en `src/main/java/com/c3/logitrack/`).
3. Haz clic derecho → **Run** o presiona `Shift+F10` (IntelliJ).

### Verificar que la app está corriendo

```powershell
curl.exe http://localhost:8080/swagger-ui.html
```

Debe devolver código `200` y mostrar la interfaz HTML de Swagger.

---

## 👥 Perfiles de Aplicación

### Perfil `prod` (Producción - Defecto)

```powershell
.\mvnw.cmd spring-boot:run
```

- Autenticación JWT estricta.
- Contraseñas verificadas.
- Modo seguro.

### Perfil `dev` (Desarrollo - Solo Local)

Permite autenticación simplificada para pruebas rápidas:

```powershell
.\mvnw.cmd spring-boot:run -Dspring.profiles.active=dev
```

O con Maven full:

```powershell
.\mvnw.cmd -DskipTests clean package -Dspring.profiles.active=dev
.\mvnw.cmd spring-boot:run -Dspring.profiles.active=dev
```

El perfil `dev` activa `src/main/resources/application-dev.properties`:

```properties
auth.allowAnyCredentials=true
```

Esto permite login con cualquier usuario/contraseña (solo para desarrollo local).

> ⚠️ **NUNCA uses el perfil `dev` en producción.** Representa un riesgo grave de seguridad.

---

## 📚 Documentación API

### Swagger UI

Accede a la interfaz interactiva de Swagger en:

```
http://localhost:8080/swagger-ui.html
```

O (ruta alternativa):

```
http://localhost:8080/swagger-ui/index.html
```

### Especificación OpenAPI

- **JSON**: `http://localhost:8080/v3/api-docs`
- **YAML**: `http://localhost:8080/v3/api-docs.yaml`

Puedes importar estas especificaciones en Postman, Insomnia o cualquier cliente REST.

---

## 🔌 Endpoints

### 📍 Autenticación

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| POST | `/auth/register` | Registrar nuevo usuario | No |
| POST | `/auth/login` | Obtener token JWT | No |
| POST | `/auth/logout` | Cerrar sesión | JWT |

**Ejemplo: Login**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Respuesta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin"
}
```

### 📍 Bodegas

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| GET | `/bodegas` | Listar todas las bodegas | No |
| GET | `/bodegas/{id}` | Obtener bodega por ID | No |
| POST | `/bodegas` | Crear nueva bodega | JWT |
| PUT | `/bodegas/{id}` | Actualizar bodega | JWT |
| DELETE | `/bodegas/{id}` | Eliminar bodega | JWT |

### 📍 Productos

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| GET | `/productos` | Listar todos los productos | No |
| GET | `/productos/{id}` | Obtener producto por ID | No |
| POST | `/productos` | Crear nuevo producto | JWT |
| PUT | `/productos/{id}` | Actualizar producto | JWT |
| DELETE | `/productos/{id}` | Eliminar producto | JWT |

### 📍 Movimientos de Inventario

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| GET | `/movimientos` | Listar movimientos | No |
| GET | `/movimientos/{id}` | Obtener movimiento por ID | No |
| POST | `/movimientos` | Registrar movimiento | JWT |
| PUT | `/movimientos/{id}` | Actualizar movimiento | JWT |
| DELETE | `/movimientos/{id}` | Eliminar movimiento | JWT |

### 📍 Auditoría

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| GET | `/auditorias` | Listar registros de auditoría | No |
| GET | `/auditorias/{id}` | Obtener registro por ID | No |

### 📍 Reportes

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| GET | `/reportes` | Obtener resumen de inventarios | No |

### 📍 Usuarios

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| GET | `/usuarios` | Listar usuarios | JWT (ADMIN) |
| GET | `/usuarios/{id}` | Obtener usuario por ID | JWT |
| PUT | `/usuarios/{id}` | Actualizar usuario | JWT |
| DELETE | `/usuarios/{id}` | Eliminar usuario | JWT (ADMIN) |

---

## 🔐 Autenticación

### Obtener Token JWT

1. Registra un usuario:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username":"juan",
    "password":"password123",
    "email":"juan@example.com"
  }'
```

2. Haz login:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"juan","password":"password123"}'
```

Obtendrás un JSON con el token:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

### Usar el Token

En cada petición protegida, añade el header `Authorization`:

```bash
curl -X GET http://localhost:8080/bodegas \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

O en Swagger UI: haz clic en el botón **"Authorize"** y pega el token.

---

## 💾 Base de Datos

### Esquema

Las tablas se crean automáticamente al iniciar (gracias a `spring.jpa.hibernate.ddl-auto=update`).

**Tablas principales:**

- `usuarios`: Usuarios del sistema (roles ADMIN/EMPLEADO)
- `bodegas`: Almacenes/bodegas
- `productos`: Artículos del inventario
- `movimientos`: Registros de entrada/salida/transferencia
- `auditorias`: Log de todas las operaciones

### Datos Iniciales

Al iniciar, `DataInitializer.java` carga datos de prueba:

```
usuario: admin, password: admin123
usuario: empleado1, password: empleado123
```

Modifica `DataInitializer.java` si necesitas cambiar los datos iniciales.

### Reiniciar la Base de Datos

Para limpiar todos los datos y empezar de cero:

```sql
DROP DATABASE logitrack;
CREATE DATABASE logitrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Luego reinicia la aplicación para que recree las tablas.

---

## 📁 Estructura del Proyecto

```
logitrack/
├── src/
│   ├── main/
│   │   ├── java/com/c3/logitrack/
│   │   │   ├── LogitrackApplication.java       # Clase principal
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java              # Configuración general
│   │   │   │   ├── AuditoriaListener.java      # Listener de auditoría
│   │   │   │   ├── DataInitializer.java        # Datos iniciales
│   │   │   │   └── SwaggerConfig.java          # Configuración Swagger/OpenAPI
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java         # Endpoints de autenticación
│   │   │   │   ├── BodegaController.java       # Endpoints de bodegas
│   │   │   │   ├── ProductoController.java     # Endpoints de productos
│   │   │   │   ├── MovimientoController.java   # Endpoints de movimientos
│   │   │   │   ├── AuditoriaController.java    # Endpoints de auditoría
│   │   │   │   ├── ReporteController.java      # Endpoints de reportes
│   │   │   │   └── UsuarioController.java      # Endpoints de usuarios
│   │   │   ├── entities/
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Bodega.java
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Movimiento.java
│   │   │   │   ├── Auditoria.java
│   │   │   │   └── enums/
│   │   │   │       ├── RolUsuario.java
│   │   │   │       ├── TipoMovimiento.java
│   │   │   │       └── TipoOperacion.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java # Manejo de errores
│   │   │   │   ├── BadRequestException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   └── DTOs...
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── BodegaRepository.java
│   │   │   │   ├── ProductoRepository.java
│   │   │   │   ├── MovimientoRepository.java
│   │   │   │   └── AuditoriaRepository.java
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java         # Configuración de seguridad
│   │   │   │   ├── JwtTokenProvider.java       # Generación/validación de JWT
│   │   │   │   ├── JwtAuthenticationFilter.java # Filtro JWT
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── service/
│   │   │       ├── UsuarioService.java
│   │   │       ├── BodegaService.java
│   │   │       ├── ProductoService.java
│   │   │       ├── MovimientoService.java
│   │   │       ├── AuditoriaService.java
│   │   │       ├── AuthService.java
│   │   │       └── ReporteService.java
│   │   └── resources/
│   │       ├── application.properties           # Configuración principal
│   │       ├── application-dev.properties       # Perfil dev
│   │       ├── schema.sql                       # Script de esquema
│   │       ├── data.sql                         # Datos iniciales (SQL)
│   │       └── static/
│   │           ├── index.html
│   │           ├── dashboard.html
│   │           ├── styles.css
│   │           └── scripts/
│   └── test/
│       └── java/com/c3/logitrack/
│           └── LogitrackApplicationTests.java
├── pom.xml                                      # Dependencias Maven
├── mvnw / mvnw.cmd                              # Maven wrapper
├── README.md                                    # Este archivo
├── SWAGGER_DOCUMENTATION.md                     # Documentación Swagger detallada
└── API_ARCHITECTURE.md                          # Diagrama de arquitectura
```

---

## 📦 Dependencias

### Dependencias Principales

- **Spring Boot 3.5.7**
- **Spring Data JPA**: Acceso a datos
- **Spring Security**: Autenticación y autorización
- **MySQL Connector**: Driver de base de datos
- **JJWT**: Librería JWT
- **SpringDoc OpenAPI 2.1.0**: Swagger/OpenAPI
- **Lombok**: Reducción de boilerplate
- **Hibernate Validator**: Validación de datos

Ver `pom.xml` para la lista completa.

---

## 🧪 Testing

### Ejecutar Tests

```powershell
.\mvnw.cmd test
```

### Tests Incluidos

- `LogitrackApplicationTests.java`: Test de contexto básico

Para agregar más tests, crea archivos en `src/test/java/`.

---

## 🛠️ Desarrollo

### Añadir Nuevas Funcionalidades

1. **Crear entidad** en `src/main/java/com/c3/logitrack/entities/`
2. **Crear repositorio** en `src/main/java/com/c3/logitrack/repository/`
3. **Crear servicio** en `src/main/java/com/c3/logitrack/service/`
4. **Crear controlador** en `src/main/java/com/c3/logitrack/controller/`
5. **Añadir anotaciones Swagger** para documentar

### Formato de Código

Usa las convenciones de Spring Boot y Java:
- Nombres PascalCase para clases
- camelCase para variables y métodos
- Sigue el patrón MVC/Service

---

## 🐛 Troubleshooting

### Error: `Connection refused` (Base de datos)

**Causa**: MySQL no está corriendo o las credenciales son incorrectas.

**Solución**:
```powershell
# Verificar que MySQL está corriendo
Get-Service | grep -i mysql

# Iniciar MySQL (si está instalado localmente)
# O conectar a tu servidor MySQL remoto y actualizar application.properties
```

### Error: `401 Unauthorized`

**Causa**: Token JWT ausente o expirado.

**Solución**:
- Obtén un nuevo token con `/auth/login`
- Verifica que el header sea: `Authorization: Bearer <token>`

### Error: `404 Not Found` en Swagger

**Causa**: `/swagger-ui.html` o `/v3/api-docs` no accesible.

**Solución**:
- Verifica que la app está corriendo en `http://localhost:8080`
- Revisa `springdoc.swagger-ui.enabled=true` en `application.properties`
- Limpia caché del navegador (Ctrl+F5)

### Error: `500 Internal Server Error` en `/v3/api-docs`

**Causa**: Problema en `SwaggerConfig.java` o serialización de modelos.

**Solución**:
- Revisa los logs de la consola
- Desactiva `AuditoriaListener` temporalmente añadiendo en `application.properties`:
  ```properties
  auditoria.enabled=false
  ```
- Verifica que no hay referencias cíclicas en las entidades

---

## 📄 Archivos de Documentación Adicional

- **`SWAGGER_DOCUMENTATION.md`**: Documentación detallada de Swagger, anotaciones y ejemplos
- **`API_ARCHITECTURE.md`**: Diagrama de arquitectura, flujo de seguridad y componentes
- **`IMPLEMENTATION_CHECKLIST.md`**: Checklist de implementación y estado del proyecto
- **`QUICK_START_SWAGGER.md`**: Guía rápida de inicio con Swagger

---

## ✅ Checklist de Desarrollo

- [x] Autenticación JWT implementada
- [x] Roles de usuario (ADMIN/EMPLEADO)
- [x] Auditoría automática
- [x] Seguridad configurada (CORS, HTTPS en prod)
- [x] Swagger/OpenAPI documentado
- [x] Validación de datos
- [x] Manejo global de excepciones
- [x] Base de datos MySQL con Hibernate
- [x] Perfiles de aplicación (dev/prod)

---

## 🤝 Contribuciones

Si deseas contribuir:

1. Crea una rama: `git checkout -b feature/nombre-feature`
2. Realiza cambios y tests
3. Commit: `git commit -am 'Añadir feature'`
4. Push: `git push origin feature/nombre-feature`
5. Crea un Pull Request

---

## 📝 Licencia

Este proyecto está licenciado bajo la **Licencia MIT** — ver `pom.xml` para detalles.

---

## 👨‍💻 Autor

**Equipo de Desarrollo LogiTrack**
- Sandry Páez
- Henry Angulo

---

## 📧 Soporte

Para soporte técnico, contacta a: `soporte@logitrack.com`

---

## 🚀 Deployment (Producción)

### Cambios Necesarios para Producción

1. **Cambiar credenciales de BD** en `application.properties`
2. **Cambiar JWT secret** (debe ser más largo y seguro)
3. **Habilitar HTTPS** en el servidor
4. **Desactivar debug logging**:
   ```properties
   logging.level.org.springframework.security=INFO
   ```
5. **Usar perfil prod**:
   ```powershell
   java -jar target/logitrack-*.jar --spring.profiles.active=prod
   ```
6. **Configurar firewall** y restricciones de red
7. **Configurar backups automáticos** de la base de datos

---

**Última actualización**: 17 de noviembre de 2025
