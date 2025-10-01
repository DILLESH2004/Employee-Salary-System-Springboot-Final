# Employee Salary System (Spring Boot + PostgreSQL)


## How to run

1. Ensure PostgreSQL is running and you have a database named `Employees`.
   ```sql
   CREATE DATABASE "Employees";
   ```

2. Update credentials in `src/main/resources/application.properties` if needed.

3. Build & run:
   ```bash
   ./mvnw spring-boot:run
   ```
   or, if Maven is installed:
   ```bash
   mvn spring-boot:run
   ```

4. API docs: open http://localhost:8080/swagger-ui.html

## REST Endpoints

- `POST /api/employees` – Create
- `GET /api/employees` – List all
- `GET /api/employees/{id}` – Get one
- `PUT /api/employees/{id}` – Update
- `DELETE /api/employees/{id}` – Delete
- `GET /api/employees/role/{role}` – Filter by role
- `GET /api/employees/metrics/total-payroll` – Sum of all salaries
