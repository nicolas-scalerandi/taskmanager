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

## Roadmap
- v0.2 — Validación y manejo de errores
- v0.3 — Autenticación con JWT
- v0.4 — Tests automatizados
- v1.0 — Docker Compose, Swagger y despliegue