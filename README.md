# TrackIt — Issue Tracking System
### PAF-IAST Final Project · BS Software Engineering

A full-stack Jira-like issue tracking system with role-based access control, JWT authentication, and a modern React frontend.

---

## Tech Stack

| Layer     | Technology                                   |
|-----------|----------------------------------------------|
| Backend   | Java 17, Spring Boot 3.2, Spring Security 6  |
| ORM       | Spring Data JPA / Hibernate 6                |
| Database  | MySQL 8                                      |
| Auth      | JWT (jjwt 0.11.5) + BCrypt                   |
| Validation| Jakarta Bean Validation                      |
| Frontend  | React 18, Vite, Tailwind CSS 3               |
| Testing   | JUnit 5, Mockito, Spring Boot Test, H2       |

---

## Architecture

```
issue-tracker/
├── backend/                  # Spring Boot application
│   └── src/main/java/com/paf/issuetracker/
│       ├── config/           # SecurityConfig (CORS, JWT, stateless)
│       ├── controller/       # REST controllers (AuthController, IssueController, …)
│       ├── dto/              # Request/Response DTOs with validation
│       ├── entity/           # JPA entities (User, Project, Issue, Comment)
│       ├── enums/            # Role, IssueStatus, IssuePriority, IssueType
│       ├── exception/        # Global exception handler
│       ├── repository/       # JPA repositories with custom JPQL queries
│       ├── security/         # JwtTokenProvider, JwtAuthFilter, UserDetailsServiceImpl
│       └── service/          # Business logic (AuthService, IssueService, …)
└── frontend/                 # Vite + React SPA
    └── src/
        ├── api/              # Axios API clients
        ├── components/       # Reusable components
        ├── context/          # AuthContext
        └── pages/            # Dashboard, Projects, ProjectDetail, IssueDetail
```

---

## REST API

| Method | Endpoint                          | Description               | Auth   |
|--------|-----------------------------------|---------------------------|--------|
| POST   | /api/auth/register                | Register new user         | Public |
| POST   | /api/auth/login                   | Login, returns JWT        | Public |
| GET    | /api/projects                     | List accessible projects  | Bearer |
| POST   | /api/projects                     | Create project            | Bearer |
| GET    | /api/projects/{id}                | Get project details       | Bearer |
| PUT    | /api/projects/{id}                | Update project            | Bearer |
| DELETE | /api/projects/{id}                | Delete project            | PM/Admin |
| POST   | /api/projects/{id}/members        | Add member to project     | Bearer |
| GET    | /api/projects/{pid}/issues        | Get project issues        | Bearer |
| POST   | /api/projects/{pid}/issues        | Create issue              | Bearer |
| GET    | /api/issues/{id}                  | Get issue detail          | Bearer |
| PUT    | /api/issues/{id}                  | Update issue              | Bearer |
| PATCH  | /api/issues/{id}/status           | Update issue status       | Bearer |
| PATCH  | /api/issues/{id}/assign           | Assign/unassign issue     | Bearer |
| DELETE | /api/issues/{id}                  | Delete issue              | Bearer |
| GET    | /api/issues/{id}/comments         | Get comments              | Bearer |
| POST   | /api/issues/{id}/comments         | Add comment               | Bearer |
| DELETE | /api/comments/{id}                | Delete comment            | Bearer |
| GET    | /api/dashboard/stats              | Dashboard statistics      | Bearer |
| GET    | /api/users/me                     | Current user profile      | Bearer |

---

## Roles & Permissions

| Role            | Capabilities                                      |
|-----------------|---------------------------------------------------|
| ADMIN           | Full system access, delete any resource           |
| PROJECT_MANAGER | Create/delete projects, manage all issues         |
| DEVELOPER       | Create/update issues, comment                     |
| REPORTER        | Report issues, add comments (default on register) |

---

## Setup & Run

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Node.js 18+

### Backend
```bash
cd backend
# Edit src/main/resources/application.properties (DB credentials)
mvn spring-boot:run
# Starts on http://localhost:8080
# DB is auto-created (createDatabaseIfNotExist=true)
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# Starts on http://localhost:5173
# Proxies /api → http://localhost:8080
```

### Run Tests
cd backend
mvn test


---

## Design Decisions

1. **Layered Architecture** — Controller → Service → Repository. Each layer has a single responsibility.
2. **SOLID Principles** — Single Responsibility in services; Open/Closed via Enums; DI throughout via @RequiredArgsConstructor.
3. **Stateless JWT Auth** — No server-side sessions; every request is authenticated via the Authorization header.
4. **Global Exception Handling** — @RestControllerAdvice catches all exceptions and returns consistent ApiResponse<T>.
5. **Transaction Management** — @Transactional on service methods with readOnly=true for GET operations.
6. **Validation** — Bean Validation annotations on all DTOs with field-level error messages.
7. **Issue Numbering** — Each project maintains its own sequential issue counter (like Jira's PROJECT-1, PROJECT-2…).
