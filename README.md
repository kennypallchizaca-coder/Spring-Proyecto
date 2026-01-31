# 🚀 Backend Spring Boot - LEXISWARE Portafolio

> ⚠️ **ARQUITECTURA 100% POSTGRESQL + JWT**  
> Este backend usa **PostgreSQL** para TODO: base de datos + autenticación.  
> **NO usa Firebase**. Autenticación con **Spring Security + JWT + BCrypt**.

---

## 📋 Stack Tecnológico

- **Backend**: Spring Boot 4.0.1 + Java 21
- **Base de Datos**: PostgreSQL 15 (Docker)
- **Autenticación**: Spring Security + JWT (100% PostgreSQL)
- **Password Hashing**: BCrypt
- **Emails**: Spring Boot Mail + SMTP (Gmail)
- **ORM**: Spring Data JPA + Hibernate
- **Build**: Gradle 8+

---

## 🔧 Prerrequisitos

- ✅ **Java 21** (LTS)
- ✅ **Gradle 8+** (incluido con wrapper `./gradlew`)
- ✅ **Docker Desktop** (para PostgreSQL)
- ✅ **Git**
- ✅ **IDE**: IntelliJ IDEA / VS Code + Java extensions

---

## 🐳 Setup PostgreSQL con Docker

### 1. Iniciar PostgreSQL

```bash
# Asegúrate de estar en la raíz del proyecto
cd c:\Users\kenny\OneDrive\Documents\SPRING-PROYECTO\proyecto

# Iniciar contenedores
docker-compose up -d

# Verificar que estén corriendo
docker-compose ps

# Deberías ver:
# portafolio-db       running   0.0.0.0:5432->5432/tcp
# portafolio-pgadmin  running   0.0.0.0:8081->80/tcp
```

### 2. Acceder a pgAdmin (Opcional)

- URL: http://localhost:8081
- Email: `admin@lexisware.com`
- Password: `admin123`

**Conectar a PostgreSQL desde pgAdmin:**
- Host: `postgres`
- Port: `5432`
- Database: `portafolio_db`
- Username: `postgres`
- Password: `postgres123`

### 3. Detener contenedores

```bash
# Detener
docker-compose down

# Detener y eliminar datos
docker-compose down -v
```

---

## ⚙️ Configuración

### 1. Variables de Entorno (Opcional)

Crear `.env` en la raíz (ya está en `.gitignore`):

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/portafolio_db
DB_USERNAME=postgres
DB_PASSWORD=postgres123

# JWT
JWT_SECRET=LEXISWARE_PORTAFOLIO_SECRET_KEY_2024_MINIMO_256_BITS_PARA_HS512_ALGORITHM
JWT_EXPIRATION=86400000

# Email (Gmail)
EMAIL_USERNAME=tu-email@gmail.com
EMAIL_APP_PASSWORD=tu-app-password-de-gmail

# CORS
ALLOWED_ORIGINS=http://localhost:5173,https://portafolio-two-snowy-24.vercel.app
```

### 2. Configurar Gmail para SMTP (Opcional pero recomendado)

1. Ir a Google Account → Security
2. Habilitar "2-Step Verification"
3. Generar "App Password" para "Mail"
4. Copiar el password de 16 caracteres
5. Agregar a `.env` como `EMAIL_APP_PASSWORD`

Si no configuras email, el registro funcionará pero no enviará emails de bienvenida.

---

## 🚀 Ejecutar el Proyecto

### 1. Compilar

```bash
# Limpiar y compilar (sin tests)
./gradlew clean build -x test

# Debería mostrar: BUILD SUCCESSFUL
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Gradle
./gradlew bootRun

# Opción 2: Con Java (JAR)
java -jar build/libs/portafolio-backend-1.0.0.jar
```

### 3. Verificar que funciona

```bash
# Health check
curl http://localhost:8080/api/public/health

# Debería responder con JSON
```

---

## 📡 API Endpoints

### Autenticación (Públicos)

```
POST   /api/auth/register    # Registro de usuario
POST   /api/auth/login       # Login
GET    /api/auth/me          # Usuario actual (requiere token)
```

### Usuarios (Protegidos - requieren JWT)

```
GET    /api/users                  # Todos los usuarios
GET    /api/users/programmers      # Solo programadores
GET    /api/users/{id}             # Usuario por ID
PUT    /api/users/me               # Actualizar perfil
PATCH  /api/users/me/availability  # Cambiar disponibilidad
```

### Proyectos (Protegidos)

```
GET    /api/projects               # Todos los proyectos
GET    /api/projects/{id}          # Proyecto por ID
GET    /api/projects/user/{uid}    # Proyectos de un usuario
POST   /api/projects               # Crear proyecto
PUT    /api/projects/{id}          # Actualizar proyecto
DELETE /api/projects/{id}          # Eliminar proyecto
```

### Asesorías (Protegidos)

```
GET    /api/advisories                    # Todas las asesorías
POST   /api/advisories                    # Crear asesoría (+ envía emails)
GET    /api/advisories/programmer/{id}    # Asesorías de un programador
PATCH  /api/advisories/{id}/approve       # Aprobar (+ envía email)
PATCH  /api/advisories/{id}/reject        # Rechazar (+ envía email)
DELETE /api/advisories/{id}               # Eliminar
```

### Públicos

```
GET    /api/public/health    # Health check
GET    /api/public/info      # Info del sistema
```

---

## 🔐 Autenticación

### Flujo de Registro

```bash
# 1. Registrarse
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "displayName": "Test User",
    "role": "PROGRAMMER"
  }'

# Respuesta:
# {
#   "token": "eyJhbGciOiJIUzUxMiJ9...",
#   "user": {
#     "uid": "uuid-generado",
#     "email": "test@example.com",
#     "displayName": "Test User",
#     "role": "PROGRAMMER"
#   }
# }
```

### Flujo de Login

```bash
# 2. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# Respuesta: igual que registro
```

### Usar el Token

```bash
# 3. Hacer request autenticado
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

## 📧 Sistema de Emails

El backend envía emails automáticamente en estos casos:

1. **Registro**: Email de bienvenida
2. **Nueva asesoría**: Email al programador + confirmación al solicitante
3. **Estado asesoría**: Email al solicitante cuando se aprueba/rechaza

**Nota**: Si no configuras SMTP, los emails no se enviarán pero la funcionalidad seguirá funcionando.

---

## 🗄️ Base de Datos

### Tablas Creadas Automáticamente

Hibernate crea estas tablas al iniciar:

- `users` - Usuarios con passwords BCrypt
- `user_skills` - Habilidades de usuarios
- `user_schedules` - Horarios de programadores
- `projects` - Proyectos
- `project_tech_stack` - Tecnologías de proyectos
- `advisories` - Solicitudes de asesoría
- `portfolios` - Portfolios de usuarios

### Conectarse a PostgreSQL

```bash
# Opción 1: psql
docker exec -it portafolio-db psql -U postgres -d portafolio_db

# Comandos útiles:
\dt              # Listar tablas
\d users         # Descripción de tabla users
SELECT * FROM users LIMIT 5;
\q               # Salir
```

---

## 🧪 Testing

### Test Manual con cURL

```bash
# 1. Registro
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"dev@test.com","password":"test123","displayName":"Dev User","role":"PROGRAMMER"}'

# 2. Login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"dev@test.com","password":"test123"}' | jq -r '.token')

# 3. Obtener usuario actual
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"
```

### Test con Postman

1. Importar collection (crear archivo `portafolio.postman_collection.json`)
2. Crear variable `{{token}}` en environment
3. Login → Copiar token → Set como variable
4. Probar endpoints protegidos

---

## 🚀 Deployment

### Railway.app

1. Crear cuenta en Railway
2. New Project → Deploy from GitHub
3. Agregar PostgreSQL addon
4. Variables de entorno:

```env
JWT_SECRET=tu-secret-seguro-256-bits
EMAIL_USERNAME=tu-email@gmail.com
EMAIL_APP_PASSWORD=tu-app-password
ALLOWED_ORIGINS=https://tu-frontend.vercel.app
```

5. Deploy automático

### Render.com

Similar a Railway, crear Web Service y PostgreSQL.

---

## 📁 Estructura del Proyecto

```
src/main/java/com/lexisware/portafolio/
├── config/
│   ├── CorsConfig.java           # Configuración CORS
│   └── SecurityConfig.java       # Spring Security + JWT
├── controller/
│   ├── AuthController.java       # /api/auth/*
│   ├── UserController.java       # /api/users/*
│   ├── ProjectController.java    # /api/projects/*
│   ├── AdvisoryController.java   # /api/advisories/*
│   ├── PortfolioController.java  # /api/portfolios/*
│   └── PublicController.java     # /api/public/*
├── dto/
│   ├── RegisterRequest.java      # DTO registro
│   ├── LoginRequest.java         # DTO login
│   └── AuthResponse.java         # DTO respuesta auth
├── entity/
│   ├── User.java                 # Usuario con password BCrypt
│   ├── Project.java
│   ├── Advisory.java
│   └── Portfolio.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── UserRepository.java       # findByEmail()
│   ├── ProjectRepository.java
│   ├── AdvisoryRepository.java
│   └── PortfolioRepository.java
├── security/
│   ├── JwtTokenProvider.java     # Genera/valida tokens JWT
│   └── JwtAuthenticationFilter.java  # Intercepta requests
├── service/
│   ├── AuthService.java          # register(), login()
│   ├── UserService.java
│   ├── ProjectService.java
│   ├── AdvisoryService.java      # + EmailService integrado
│   ├── PortfolioService.java
│   └── EmailService.java         # Envío de emails SMTP
└── PortafolioBackendApplication.java
```

---

## 🔧 Troubleshooting

### Error: "Connection refused" (PostgreSQL)

```bash
# Verificar que Docker esté corriendo
docker ps

# Reiniciar contenedores
docker-compose down
docker-compose up -d
```

### Error: "JWT signature does not match"

- Verificar que `jwt.secret` sea el mismo en todas partes
- El token expira en 24 horas, generar uno nuevo con login

### Error: "Mail server connection failed"

- Verificar `EMAIL_USERNAME` y `EMAIL_APP_PASSWORD`
- Usar "App Password" de Gmail, no la contraseña normal
- Si no quieres emails, la app funciona igual

### IDE no reconoce imports

```bash
# Refrescar dependencias
./gradlew --refresh-dependencies clean build -x test

# En IntelliJ: File → Invalidate Caches / Restart
# En VS Code: Reload Window
```

---

## 📚 Recursos

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)
- [Docker Compose](https://docs.docker.com/compose/)

---

## 📝 Migración desde Firebase

Este proyecto reemplazó completamente Firebase con:

- ❌ **Firebase Auth** → ✅ **JWT + BCrypt + PostgreSQL**
- ❌ **Firestore** → ✅ **PostgreSQL + JPA**
- ❌ **Firebase Admin SDK** → ✅ **Spring Security**

**Ventajas**:
- ✅ Control total de datos
- ✅ Sin dependencias externas
- ✅ Más barato en producción
- ✅ Mejor para casos de uso enterprise

---

## 👥 Contribuir

1. Fork el proyecto
2. Crear feature branch
3. Commit cambios
4. Push a branch
5. Abrir Pull Request

---

## 📄 Licencia

MIT License - LEXISWARE 2024
