# LogiTrack (desarrollo local)

Proyecto Spring Boot para gestión de inventarios (LogiTrack).

## Build y ejecución

Desde PowerShell en Windows, en la carpeta `logitrack`:

```powershell
cd c:\Users\carol\OneDrive\Desktop\LogiTrack_SandryPaez_HenryAngulo\logitrack
.\mvnw.cmd -DskipTests package
.\mvnw.cmd spring-boot:run
```

Para activar el modo _dev_ que permite iniciar sesión con cualquier usuario/contraseña (SOLO PARA PRUEBAS LOCALES):

```powershell
.\mvnw.cmd -DskipTests package -Dspring.profiles.active=dev
.\mvnw.cmd spring-boot:run -Dspring.profiles.active=dev
```

> ADVERTENCIA: El perfil `dev` habilita `auth.allowAnyCredentials=true` y permite el acceso sin verificar la contraseña. No usar en entornos de producción.

## Endpoints públicos (GET)

- `/bodegas` y `/bodegas/{id}`
- `/productos` y `/productos/{id}`
- `/movimientos` y `/movimientos/{id}`
- `/auditorias` y `/auditorias/{id}`
- `/reportes` (resumen)
- `/usuarios` y `/usuarios/{id}`

Las demás operaciones (POST/PUT/DELETE) requieren autenticación JWT.

## Swagger

Si la aplicación está corriendo, la UI de Swagger está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

## Notas sobre cambios realizados

- Se añadió la propiedad `auth.allowAnyCredentials` controlada por perfil `dev`.
- Archivo `src/main/resources/application-dev.properties` creado con `auth.allowAnyCredentials=true`.
- Se verificaron usos de `Optional` para evitar llamadas inseguras a `.get()`.

Si quieres que revierta la lógica insegura o la altere de otra forma (por ejemplo, token estático para pruebas), dime cómo prefieres proceder.
