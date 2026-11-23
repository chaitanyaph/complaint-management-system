# Complaint Management System — Spring Boot REST API

Submit complaints, assign employees and track resolution.

**Entities:** `Customer`, `Complaint`, `Employee`.

## What it does
- Add customers and employees (unique emails).
- A customer submits a complaint. It starts as `OPEN`.
- Assign it to an employee (`ASSIGNED`; it can be reassigned while still `ASSIGNED`), start work (`IN_PROGRESS`), resolve it with a resolution text (`RESOLVED`, with a resolved time), then close it (`CLOSED`). A resolved complaint can be reopened to `IN_PROGRESS`.
- Steps cannot be skipped, and `CLOSED` is final. Invalid moves return `409`.
- List complaints, filtered by status and/or assigned employee.

## Endpoints
| Method | Path | Purpose |
|---|---|---|
| POST | `/api/customers` | Add a customer |
| GET | `/api/customers`, `/api/customers/{id}` | List / get |
| POST | `/api/employees` | Add an employee |
| GET | `/api/employees`, `/api/employees/{id}` | List / get |
| POST | `/api/complaints` | Submit: `{"customerId":1,"title":"Late delivery","description":"..."}` |
| GET | `/api/complaints?status=OPEN&employeeId=2` | List with optional filters |
| GET | `/api/complaints/{id}` | Get one |
| PUT | `/api/complaints/{id}/assign` | Assign: `{"employeeId":2}` |
| PATCH | `/api/complaints/{id}/status` | Set `IN_PROGRESS` or `CLOSED` |
| PUT | `/api/complaints/{id}/resolve` | Resolve: `{"resolution":"Refund issued"}` |

## Tech stack
Java 17, Spring Boot 3.5, Spring Data JPA, Bean Validation, H2 / MySQL, springdoc-openapi, JUnit 5 + MockMvc.

## Run
Prerequisites: JDK 17+ and Maven. No database setup is needed: by default it uses an in-memory H2 database.

```bash
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui.html

To use MySQL instead, export your own credentials and start with the `mysql` profile (nothing is committed):

```bash
export DB_USERNAME=your_user DB_PASSWORD=your_password
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Test
```bash
mvn test
```

## Notes
Backend only, built as a portfolio project. There is no authentication and no front end. Error responses are JSON: `404` for unknown ids, `409` for business-rule violations or duplicates, and `400` with a `fields` map for validation errors.
