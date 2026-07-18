![CI](https://github.com/nicolas-scalerandi/taskmanager/actions/workflows/ci.yml/badge.svg)

# TaskManager API

*[Leer en español](README.md)*

REST API built with Spring Boot 3, Spring Data JPA and PostgreSQL.
A project for learning Spring Boot in depth.

**🔗 Live demo:** https://taskmanager-production-ee00.up.railway.app/swagger-ui/index.html

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
- Deployed on Railway

## How to run it

### Option 1 — Locally with IntelliJ
1. Have Docker running
2. `docker run --name taskmanager-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=taskmanager -p 5432:5432 -d postgres`
3. Run the app from IntelliJ

### Option 2 — With Docker Compose
1. Have Docker Desktop running
2. `docker-compose up`
3. The app and the database start together automatically

## Endpoints v0.1
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/tasks | Lists the authenticated user's tasks |
| GET | /api/tasks/{id} | Retrieves a task |
| POST | /api/tasks | Creates a task |
| PUT | /api/tasks/{id} | Updates a task |
| DELETE | /api/tasks/{id} | Deletes a task |

## Error handling v0.2
All errors return a structured JSON body:
​```json
{
    "status": 400,
    "message": "title: must not be blank",
    "timestamp": "2026-05-25T19:11:22"
}
​```

### Response codes
| Code | Situation |
|--------|-----------|
| 200 | OK |
| 201 | Task created |
| 400 | Validation failed or invalid body |
| 401 | Invalid credentials (login) |
| 404 | Task not found |

## Authentication v0.3
Stateless JWT authentication. Registration, login and endpoint protection. Each user can only see and manage their own tasks.

### Authentication endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/auth/register | Creates a user, returns a JWT token |
| POST | /api/auth/login | Validates credentials, returns a JWT token |

### Usage
Include the token in every protected request:
```
Authorization: Bearer {token}
```

## Tests v0.4
Automated test suite with JUnit 5, Mockito and MockMvc.

- **TaskServiceTest**: unit tests for business logic (Mockito, no database)
- **TaskControllerTest**: web-layer tests with `@WebMvcTest` + `MockMvc`, simulating real HTTP requests
- **TaskmanagerApplicationTests**: full context test, using an in-memory H2 database (`test` profile)

Run tests locally:
mvn test

CI runs the full suite on every push to `main` (see badge above).

## Documentation, Docker and deployment v1.0

### Swagger / OpenAPI
Interactive documentation auto-generated with Springdoc, available at `/swagger-ui.html` (or `/swagger-ui/index.html`). Includes an **Authorize** button to test protected endpoints by pasting the JWT token just once.

### DTOs (Request/Response)
The `Task` endpoints don't expose the JPA entity directly. `TaskRequest` defines what the client can send (title, description, status) and `TaskResponse` defines what the API returns (no user data or password). This keeps the Swagger examples clean and avoids leaking sensitive information in responses.

### Docker
- **Dockerfile**: multistage build (compiles with Maven+JDK in one stage, runs with just the JRE in the final stage for a lightweight image)
- **docker-compose.yml**: brings up the app and PostgreSQL together with a single command

### Deployment
The app is deployed on **Railway**, with a managed PostgreSQL database connected automatically. Public URL above, in the Live Demo section.

## Roadmap
- ✅ v0.1 — Working CRUD
- ✅ v0.2 — Validation and error handling
- ✅ v0.3 — JWT authentication
- ✅ v0.4 — Automated tests
- ✅ v1.0 — Docker Compose, Swagger and deployment
- v1.1 — Basic React frontend
- v1.2 — Frontend auth + UX improvements
- v1.3 — Pagination and filters
- v1.4 — ADMIN / USER roles