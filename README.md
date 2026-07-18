![CI](https://github.com/nicolas-scalerandi/taskmanager/actions/workflows/ci.yml/badge.svg)

# TaskManager API

*[Read in English](README.en.md)*

REST API construida con Spring Boot 3, Spring Data JPA y PostgreSQL.
Proyecto para aprender Spring Boot.

**🔗 Demo en vivo:** https://taskmanager-production-ee00.up.railway.app/swagger-ui/index.html

## Stack
- Java 17
- Spring Boot 3.5
- Spring Data JPA + Hibernate
- PostgreSQL (Docker)
- Spring Security + JWT (jjwt)
- JUnit 5 + Mockito + MockMvc + AssertJ
- Docker + Docker Compose
- Springdoc OpenAPI (Swagger UI)
- GitHub Actions (CI)
- Despliegue en Railway

## Cómo ejecutar

### Opción 1 — Local con IntelliJ
1. Tener Docker corriendo
2. `docker run --name taskmanager-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=taskmanager -p 5432:5432 -d postgres`
3. Ejecutar la app desde IntelliJ

### Opción 2 — Con Docker Compose
1. Tener Docker Desktop corriendo
2. `docker-compose up`
3. La app y la base de datos se levantan juntas automáticamente

## Endpoints v0.1
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/tasks | Lista las tareas del usuario autenticado |
| GET | /api/tasks/{id} | Obtiene una tarea |
| POST | /api/tasks | Crea una tarea |
| PUT | /api/tasks/{id} | Actualiza una tarea |
| DELETE | /api/tasks/{id} | Elimina una tarea |

## Manejo de errores v0.2
Todos los errores devuelven JSON estructurado:
​```json
{
    "status": 400,
    "message": "title: no debe estar vacío",
    "timestamp": "2026-05-25T19:11:22"
}
​```

### Códigos de respuesta
| Código | Situación |
|--------|-----------|
| 200 | OK |
| 201 | Tarea creada |
| 400 | Validación fallida o body inválido |
| 401 | Credenciales inválidas (login) |
| 404 | Tarea no encontrada |

## Autenticación v0.3
JWT stateless authentication. Registro, login y protección de endpoints. Cada usuario solo ve y gestiona sus propias tareas.

### Endpoints de autenticación
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/auth/register | Crea usuario, devuelve token JWT |
| POST | /api/auth/login | Valida credenciales, devuelve token JWT |

### Uso
Incluir el token en cada request protegido:
```
Authorization: Bearer {token}
```

## Tests v0.4
Suite de tests automatizados con JUnit 5, Mockito y MockMvc.

- **TaskServiceTest**: tests unitarios de la lógica de negocio (Mockito, sin base de datos)
- **TaskControllerTest**: tests de la capa web con `@WebMvcTest` + `MockMvc`, simulando peticiones HTTP reales
- **TaskmanagerApplicationTests**: test de contexto completo, usando H2 en memoria (perfil `test`)

Ejecutar tests localmente:
mvn test

CI ejecuta la suite completa en cada push a `main` (ver badge arriba).

## Documentación, Docker y despliegue v1.0

### Swagger / OpenAPI
Documentación interactiva generada automáticamente con Springdoc, disponible en `/swagger-ui.html` (o `/swagger-ui/index.html`). Incluye botón **Authorize** para probar los endpoints protegidos pegando el token JWT una sola vez.

### DTOs (Request/Response)
Los endpoints de `Task` no exponen la entidad JPA directamente. `TaskRequest` define lo que el cliente puede enviar (title, description, status) y `TaskResponse` define lo que la API devuelve (sin datos del usuario ni contraseña). Esto mantiene los ejemplos de Swagger limpios y evita filtrar información sensible en las respuestas.

### Docker
- **Dockerfile**: build multistage (compila con Maven+JDK en una etapa, corre solo con JRE en la etapa final para una imagen liviana)
- **docker-compose.yml**: levanta la app y PostgreSQL juntos con un solo comando

### Despliegue
App desplegada en **Railway**, con base de datos PostgreSQL gestionada y conectada automáticamente. URL pública arriba, en la sección de Demo en vivo.

## Roadmap
- ✅ v0.1 — CRUD funcional
- ✅ v0.2 — Validación y manejo de errores
- ✅ v0.3 — Autenticación con JWT
- ✅ v0.4 — Tests automatizados
- ✅ v1.0 — Docker Compose, Swagger y despliegue
- v1.1 — Frontend React básico
- v1.2 — Auth en frontend + mejoras UX
- v1.3 — Paginación y filtros
- v1.4 — Roles ADMIN / USER