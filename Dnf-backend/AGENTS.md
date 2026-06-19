# AGENTS.md

## Quick orientation

- This is a **Java Spring Boot microservices** repo orchestrated with Docker Compose.
- **There is no root `pom.xml` or Maven multi-module setup.** Each service directory is a standalone Maven project with its own `pom.xml` and `mvnw` wrapper.
- The root `src/` and `target/` directories are leftover scaffolding and are not part of any active build.
- `Ms-comunidad` and `Ms-notificaciones` have a nested directory structure (`Ms-comunidad/Ms-comunidad/`, `Ms-notificaciones/Ms-notificaciones/`). The `docker-compose.yml` build context points to the inner directory.

## Build & run

- **Run everything (including DB) from repo root:**
  ```bash
  docker compose up --build
  ```
  - Services start in dependency order, but `depends_on` does **not** wait for Postgres to be ready. The Java services may restart until the DB accepts connections.
  - Eureka takes ~10s to start. Other services will fail to register until Eureka is up, then retry automatically.
  - After a full restart, the Gateway may need its own restart (`docker compose restart api-gateway`) to pick up Eureka registrations, or just wait ~30s for the cache to refresh.
- **Run only the database** (for local service dev):
  ```bash
  docker compose -f docker-compose-db.yml up
  ```
  - DB port is **5433** on localhost (maps to 5432 inside Docker).
- **Build a single service locally:**
  ```bash
  cd <service-dir>
  ./mvnw -DskipTests package
  ```
- **Run a single service locally** (after starting the DB separately):
  ```bash
  cd <service-dir>
  ./mvnw spring-boot:run
  ```

## Ports & service map

| Service | Local port | Spring Boot | Spring Cloud | API base path | Notes |
|---|---|---|---|---|---|
| `eurekaserver` | 8761 | 4.0.5 | 2025.1.1 | — | Discovery server |
| `Api-Gateway` | 8080 | 3.2.3 | 2023.0.1 | — | Spring Cloud Gateway **MVC** variant |
| `Ms-mascota` | 8081 | 3.2.3 | 2023.0.1 | `/api/pets` | JPA + PostgreSQL |
| `Ms-usuario` | 8082 | 3.2.3 | 2023.0.1 | `/api/usuarios` | JPA + PostgreSQL |
| `Ms-coincidencias` | 8083 | 3.2.3 | 2023.0.1 | `/api/coincidencias` | JPA + PostgreSQL, OpenFeign, Resilience4j |
| `Ms-comunidad` | 8094 | 3.2.3 | 2023.0.1 | `/api/success-stories` | JPA + PostgreSQL |
| `Ms-notificaciones` | 8095 | 4.0.5 | 2025.1.1 | `/api/notificaciones` | Email service, no DB |
| `postgres-db` | 5433 | — | — | — | `dnf_db`, user `admin` / `adminpassword` |

- Inside Docker, DB host is `postgres-db`; Eureka host is `eureka-server`.

## Gateway routing (critical gotcha)

- The Gateway uses `spring-cloud-starter-gateway-mvc` (servlet-based), **not** the reactive `spring-cloud-gateway-server`.
- **Route config prefix is `spring.cloud.gateway.mvc.routes`**, not `spring.cloud.gateway.routes` (that's the reactive variant) and not `spring.cloud.gateway.server.mvc.routes`.
- Routes use load-balanced URIs (`lb://MS-USUARIO`). The service name must match the Eureka registration name exactly (uppercase: `MS-USUARIO`, `MS-MASCOTA`, etc.).
- Current gateway routes:
  - `/api/usuarios/**`, `/api/auth/**` → `MS-USUARIO`
  - `/api/pets/**` → `MS-MASCOTA`
  - `/api/coincidencias/**` → `MS-COINCIDENCIAS`
  - `/api/success-stories/**` → `MS-COMUNIDAD`

## Testing

- Tests live inside each service. There is no root test runner.
- **Run one service's tests:**
  ```bash
  cd <service-dir>
  ./mvnw test
  ```
- Existing tests are a mix of plain `@SpringBootTest` smoke tests and focused `@WebMvcTest` controller tests with `MockMvc` + Mockito. There are no integration tests against a running DB or Eureka.

## Database & JPA

- All DB-backed services share the same PostgreSQL instance (`dnf_db`).
- `spring.jpa.hibernate.ddl-auto: update` is enabled everywhere. **There are no Flyway/Liquibase migrations.** Schema changes are applied automatically on startup.
- If you add `@Valid` or custom validation, remember that `spring-boot-starter-validation` is already present in several microservices (e.g. `eurekaserver`, `Ms-mascota`, `Ms-usuario`, `Ms-coincidencias`). Ensure it is included in your `pom.xml` if needed in others.

## Service-to-service calls

- `Ms-coincidencias` calls `ms-mascota` via **OpenFeign** (`@FeignClient(name = "ms-mascota")`) resolved through Eureka. Do not hardcode URLs.
- `Ms-notificaciones` exposes `POST /api/notificaciones/enviar-alerta` for email notifications. It does not have a DB.

## Message Broker (RabbitMQ)

-   `Ms-mascota` y `Ms-notificaciones` utilizan RabbitMQ para la comunicación asíncrona.
-   `Ms-mascota` envía eventos relacionados con mascotas (ej. mascotas perdidas/encontradas).
-   `Ms-notificaciones` consume estos eventos para enviar notificaciones.
-   La integración se realiza mediante `spring-boot-starter-amqp`.
-   La configuración de RabbitMQ para `Ms-mascota` se encuentra en `src/main/resources/application.yml` (host, port, user, pass).

## Caching (Redis)

-   Varios microservicios (`Ms-mascota`, `Ms-usuario`, `Ms-coincidencias`, `Ms-comunidad`) implementan caching con Redis para mejorar el rendimiento.
-   Utilizan las dependencias `spring-boot-starter-data-redis` y `spring-boot-starter-cache`.
-   El servicio Redis se define en `docker-compose.yml` (`dnf-redis`).
-   La configuración específica del cache se maneja en clases como `RedisConfig.java` en cada servicio.
-   Se usan anotaciones como `@Cacheable` para cachear resultados de métodos y `@CacheEvict` para invalidar la caché.

## Code Coverage (JaCoCo)

-   La mayoría de los microservicios (`Ms-notificaciones`, `Ms-mascota`, `Ms-coincidencias`, `Ms-usuario`, `Ms-comunidad`) incluyen el plugin JaCoCo Maven (`jacoco-maven-plugin`).
-   Esto permite la generación de informes de cobertura de código durante la fase de `package` de Maven, ayudando a asegurar la calidad del código.

## JPQL gotcha with null parameters

- When writing `@Query` with nullable parameters (`:param IS NULL OR column = :param`), PostgreSQL can fail with `function lower(bytea) does not exist` because it can't infer the type of a null binding.
- **Fix:** Use `COALESCE(:param, '')` for string LIKE expressions instead of `:param IS NULL OR ...`. Or cast the parameter explicitly.

## Code conventions & style

- Controllers in this repo use **field injection** (`@Autowired` on fields). Do not refactor existing classes to constructor injection unless asked.
- Entity models do **not** use Lombok; they use manual getters/setters/constructors. Only `Ms-coincidencias` DTOs currently use Lombok `@Data`.
- `Ms-usuario` and `Ms-coincidencias` POMs already configure the Lombok annotation processor. `Ms-mascota` does not include Lombok at all.
- Package names use inconsistent casing and underscores (e.g., `com.usuario.Ms_usuario`, `com.dcknotfnd.ms_coincidencias`). Follow the existing convention in each service rather than trying to normalize.
- Java class names must match their filenames exactly (case-sensitive). A file named `historia.java` with `public class Historia` will fail compilation.

## Swagger / OpenAPI

- Springdoc OpenAPI is included in the business microservices (`Ms-*`).
- Swagger UI is available at `http://localhost:<service-port>/swagger-ui.html` (or `/swagger-ui/index.html`).

## Important notes

- `AGENTS.md` is currently listed in `.gitignore`. If you want changes tracked, update `.gitignore` as well.
- No `.env` file or externalized config exists yet; DB credentials and Eureka URLs are hardcoded in `application.yml` files.
