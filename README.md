# KyrosBack

API REST del sistema **Kyros**, construida con Spring Boot, para la gestión administrativa y académica de una institución educativa: estudiantes, grupos, asesorías, cursos de verano, catálogos de universidades/carreras/bachilleratos/secundarias, usuarios, cargos, pagos y bitácora (logs).

## Tecnologías

- **Java 17**
- **Spring Boot 4.1.0** (Web MVC, Data JPA, Validation, Security)
- **MySQL 8** como base de datos
- **Flyway** para versionado y migración del esquema (`flyway-mysql`)
- **Spring Security** con autenticación **JWT** (`jjwt` 0.12.6) y contraseñas cifradas con **BCrypt**
- **Lombok**
- **Maven** (con Maven Wrapper `mvnw`)
- Empaquetado como **WAR** (con `ServletInitializer` para despliegue en contenedor externo, además de arranque embebido)

## Requisitos previos

- JDK 17+
- Docker y Docker Compose (para levantar MySQL fácilmente) — o una instancia de MySQL 8 propia
- No es necesario tener Maven instalado: el proyecto incluye el wrapper (`./mvnw`)

## Estructura del proyecto

```
src/main/java/com/kyros/demokyros/
├── config/          # Seguridad (SecurityConfig), CORS, servir archivos subidos (WebConfig)
├── convertors/       # AttributeConverter de JPA para enums (persistencia por código numérico)
├── dto/              # Objetos de transferencia expuestos por la API
├── entity/           # Entidades JPA mapeadas a las tablas de MySQL
├── enums/            # Catálogos de dominio (roles, estatus, horarios, etc.)
├── exception/        # Excepciones de negocio y manejador global (GlobalExceptionHandler)
├── form/             # Formularios/DTO de entrada para creación y actualización
├── jwt/              # Generación/validación de JWT y filtro de autenticación
├── repository/       # Repositorios Spring Data JPA
├── requests/         # Payloads de peticiones especiales (ej. login)
├── responses/        # Payloads de respuesta especiales (ej. JwtResponse)
├── restcontrollers/   # Controladores REST (un controlador por recurso)
└── services/         # Lógica de negocio

src/main/resources/
├── application.properties
└── db/migration/      # Scripts de Flyway (V1..V4)
```

## Configuración del entorno

El proyecto lee su configuración desde variables de entorno (con valores por defecto para desarrollo). Copia el archivo de ejemplo y ajústalo:

```bash
cp .env.example .env
```

Variables disponibles (`.env`):

| Variable | Descripción | Valor por defecto (dev) |
|---|---|---|
| `DB_HOST` | Host de MySQL | `localhost` |
| `DB_PORT` | Puerto de MySQL | `3306` |
| `DB_NAME` | Nombre de la base de datos | `demokyros` |
| `DB_USERNAME` | Usuario de MySQL | `demokyros` |
| `DB_PASSWORD` | Contraseña de MySQL | — (definir) |
| `MYSQL_ROOT_PASSWORD` | Contraseña root del contenedor MySQL (solo Docker Compose) | — (definir) |
| `JWT_SECRET` | Secreto para firmar los JWT (mínimo 32 bytes en producción) | clave de desarrollo incluida |
| `JWT_EXPIRATION_MINUTES` | Minutos de vigencia del token | `720` |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos por CORS, separados por coma (`*` solo en dev) | `*` |
| `UPLOADS_DIR` | Carpeta donde se guardan los archivos subidos (ej. fotos de estudiantes) | `uploads` |

> El archivo `.env` está en `.gitignore`: nunca subas credenciales reales al repositorio. Para producción, genera un `JWT_SECRET` aleatorio, por ejemplo con `openssl rand -base64 32`.

## Cómo ejecutar el proyecto

### 1. Levantar la base de datos

Con Docker Compose (usa las variables definidas en `.env`):

```bash
docker compose up -d
```

Esto crea un contenedor MySQL 8 (`demokyros-mysql`) con un volumen persistente y expone el puerto `3306` (o el definido en `DB_PORT`).

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

Al arrancar, Flyway aplica automáticamente las migraciones pendientes en `src/main/resources/db/migration` sobre la base de datos configurada. La API queda disponible en `http://localhost:8080`.

### 3. Compilar / empaquetar

```bash
./mvnw clean package
```

Genera un artefacto `.war` en `target/`, desplegable tanto de forma embebida (`java -jar`) como en un servidor de aplicaciones externo (gracias a `ServletInitializer`).

### 4. Ejecutar pruebas

```bash
./mvnw test
```

## Autenticación y autorización

La API usa JWT con sesiones **stateless** (sin cookies de sesión). El flujo es:

1. `POST /api/auth/login` con credenciales (`LoginRequest`) — endpoint público — devuelve un `JwtResponse` con el token.
2. En el resto de peticiones, enviar el token en la cabecera `Authorization: Bearer <token>`.

Reglas de acceso (`SecurityConfig`):

- `OPTIONS /**` y `POST /api/auth/login` — públicos.
- `GET /uploads/**` (archivos subidos, ej. fotos) — público.
- `POST /api/usuarios` — requiere rol `ADMIN` o `COORDINADOR`.
- Cualquier otra ruta — requiere autenticación (token válido).

Roles disponibles (`RolUsuario`): `ADMIN`, `COORDINADOR`, `SECRETARIO`, `PROFESOR`.

## Recursos de la API

Todas las rutas cuelgan de `/api`. CRUD estándar = `GET` (listar), `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`.

| Recurso | Ruta base | Notas |
|---|---|---|
| Autenticación | `/api/auth` | `POST /login` |
| Estudiantes | `/api/estudiantes` | CRUD; `POST /{id}/foto` (multipart) para subir foto; `GET/POST/DELETE /{id}/destinos/{idDestino}` para asociar destinos |
| Grupos | `/api/grupos` | CRUD; `GET /{id}/estudiantes` |
| Asesorías | `/api/asesorias` | CRUD; `GET/POST/DELETE /{id}/materias/{idMateria}` |
| Materias | `/api/materias` | CRUD |
| Cursos de verano | `/api/cursos-verano` | CRUD |
| Bachilleratos | `/api/bachilleratos` | CRUD |
| Secundarias | `/api/secundarias` | CRUD |
| Universidades | `/api/universidades` | CRUD |
| Carreras | `/api/carreras` | CRUD; `GET/POST/DELETE /{id}/universidades/{idUniversidad}` |
| Áreas | `/api/areas` | CRUD |
| Usuarios | `/api/usuarios` | CRUD; `PUT /{id}/password` |
| Cargos | `/api/cargos` | Listar, obtener, `GET /estudiante/{idEstudiante}`, crear |
| Pagos | `/api/pagos` | Listar, obtener, `GET /cargo/{idCargo}`, crear |
| Logs | `/api/logs` | Listar, `GET /usuario/{idUsuario}` |

### Subida de archivos

Las imágenes (ej. foto de estudiante) se suben vía `multipart/form-data` y se guardan en el directorio configurado por `UPLOADS_DIR` (`WebConfig` las expone como recurso estático en `/uploads/**`). Límite de tamaño: **5 MB** por archivo y por petición (`spring.servlet.multipart.max-file-size` / `max-request-size`).

### Manejo de errores

`GlobalExceptionHandler` centraliza las respuestas de error en JSON:

| Situación | Código HTTP |
|---|---|
| Recurso no encontrado (`ResourceNotFoundException`) | 404 |
| Credenciales inválidas (`InvalidCredentialsException`) | 401 |
| Validación de campos (`MethodArgumentNotValidException`) | 400 (mapa `campo → mensaje`) |
| Argumento inválido (`IllegalArgumentException`) | 400 |
| Archivo excede tamaño permitido | 413 |
| Violación de integridad de datos (registro en uso/duplicado) | 409 |

## Modelo de datos

Migraciones Flyway (`src/main/resources/db/migration`):

- **V1** — `grupo`, `estudiante`
- **V2** — `area`, `universidad`, `carrera`, `carrera_universidad`, `estudiante_universidad`
- **V3** — `bachillerato`, `secundaria`, `curso_verano`, `materia`, `asesoria`, `estudiante_bachillerato`, `estudiante_secundaria`, `estudiante_curso_verano`, `estudiante_asesoria`, `asesoria_materia`
- **V4** — `usuario`, `cargo`, `pago`, `log`

Catálogos de dominio (enums, persistidos por código numérico mediante `AttributeConverter`):

- `RolUsuario`: ADMIN, COORDINADOR, SECRETARIO, PROFESOR
- `EstatusCargo`: PENDIENTE, PARCIAL, PAGADO, VENCIDO
- `MetodoPago`: EFECTIVO, TRANSFERENCIA
- `TipoMensualidadCargo`: PAGO_COMPLETO, BIMESTRE, MENSUALIDAD, POR_HORA
- `Horario`: ESCOLARIZADO, SABATINO, VIRTUAL
- `DiaSemana`: LUNES...SABADO
- `IngresoA`: UNIVERSIDAD, BACHILLERATO, SECUNDARIA, ASESORIAS, CURSO_VERANO
