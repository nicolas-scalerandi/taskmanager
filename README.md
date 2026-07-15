![CI](https://github.com/nicolas-scalerandi/taskmanager/actions/workflows/ci.yml/badge.svg)

# TaskManager API

REST API construida con Spring Boot 3, Spring Data JPA y PostgreSQL.
Proyecto para aprender Spring Boot.

## Stack
- Java 17
- Spring Boot 3.5
- Spring Data JPA + Hibernate
- PostgreSQL (Docker)

## Cómo ejecutar
1. Tener Docker corriendo
2. `docker run --name taskmanager-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=taskmanager -p 5432:5432 -d postgres`
3. Ejecutar la app desde IntelliJ

## Endpoints v0.1
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/tasks | Lista todas las tareas |
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
| 404 | Tarea no encontrada |

## Autenticación v0.3
JWT stateless authentication. Registro, login y protección de endpoints.

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

## Roadmap
- ✅ v0.1 — CRUD funcional
- ✅ v0.2 — Validación y manejo de errores
- ✅ v0.3 — Autenticación con JWT
- v0.4 — Tests automatizados
- v1.0 — Docker Compose, Swagger y despliegue
- v1.1 — Frontend React básico
- v1.2 — Auth en frontend + mejoras UX
- v1.3 — Paginación y filtros
- v1.4 — Roles ADMIN / USER