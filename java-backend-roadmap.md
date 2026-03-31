# Java Backend Engineer — Learning Roadmap

> **Profile:** Frontend developer (JS/TS) → Java Backend Engineer
> **Pace:** Part-time, 10–20 hrs/week
> **Timeline:** 3–6 months (aggressive)
> **Focus areas:** Microservices & APIs · Databases · System Design

---

## How to Use This Roadmap

Each phase builds on the previous one. Spend roughly the suggested number of weeks per phase, but adjust based on how comfortable you feel. The ✅ checkboxes are milestone markers — don't move on until you can confidently check them off.

---

## Phase 1 — Java Core Fundamentals (Weeks 1–3)

**Why:** You already think like a programmer. This phase is about translating your JS/TS mental model into Java's world — static typing, OOP, and the JVM.

### What to Learn

- **Java syntax & type system** — primitives, classes, interfaces, enums, generics (think of generics as stricter, more powerful TypeScript generics)
- **OOP deep dive** — inheritance, polymorphism, abstract classes vs interfaces, encapsulation (Java leans much harder into OOP than JS)
- **Collections framework** — `List`, `Set`, `Map`, `Queue` and when to use each
- **Exception handling** — checked vs unchecked exceptions (no equivalent in JS — this will feel new)
- **Java 17+ features** — records, sealed classes, pattern matching, text blocks, `var` keyword
- **Build tooling** — Maven or Gradle (start with Maven; it's Gradle later if your team uses it). Comparable to npm/yarn but XML/Groovy-based.

### JS/TS → Java Mental Shortcuts

| JS/TS Concept | Java Equivalent |
|---|---|
| `interface` / `type` | `interface` / `class` |
| `npm` / `package.json` | Maven / `pom.xml` |
| `async/await` | `CompletableFuture` (later) |
| `Array.map/filter` | Streams API (Phase 2) |
| `null` / `undefined` | `null` / `Optional<T>` |
| `ESLint` | Checkstyle, SpotBugs |

### Resources

- **Book:** *Head First Java* (quick ramp-up) or *Java: A Beginner's Guide* by Herbert Schildt
- **Interactive:** [JetBrains Academy](https://hyperskill.org/) — Java track (project-based)
- **Video:** Amigoscode — Java for Beginners (YouTube, free)
- **Practice:** LeetCode Easy problems in Java (do 2–3 per week to build muscle memory)

### ✅ Phase 1 Milestones

- [ ] Build a CLI todo-list app with file persistence (JSON read/write)
- [ ] Solve 10 LeetCode Easy problems in Java
- [ ] Understand `pom.xml` and can add dependencies via Maven

---

## Phase 2 — Java Intermediate + Spring Boot Intro (Weeks 4–7)

**Why:** Spring Boot is the industry standard for Java backend. This is where you start building real APIs — similar to Express/Nest.js but with more structure and convention.

### What to Learn

- **Streams API & lambdas** — `map`, `filter`, `reduce`, `collect` (you'll feel right at home from JS)
- **Concurrency basics** — threads, `ExecutorService`, `CompletableFuture` (async in Java)
- **Spring Boot fundamentals:**
  - Project setup with [Spring Initializr](https://start.spring.io/)
  - Dependency Injection & IoC container (`@Component`, `@Service`, `@Repository`, `@Autowired`)
  - REST controllers (`@RestController`, `@GetMapping`, `@PostMapping`)
  - Request/response DTOs, validation (`@Valid`, `@NotNull`)
  - Application properties & profiles (`application.yml`)
  - Error handling (`@ControllerAdvice`, `@ExceptionHandler`)
- **Testing:** JUnit 5 + Mockito for unit tests (equivalent to Jest + mocks)

### JS/TS → Spring Boot Mental Shortcuts

| Express / NestJS | Spring Boot |
|---|---|
| `app.get('/users')` | `@GetMapping("/users")` |
| Middleware | Filters & Interceptors |
| NestJS modules/providers | Spring `@Configuration` / `@Bean` |
| `.env` files | `application.yml` + `@Value` |
| Jest | JUnit 5 + Mockito |

### Resources

- **Course:** [Spring Boot 3 & Spring Framework 6](https://www.udemy.com/course/spring-hibernate-tutorial/) by Chad Darby (Udemy) — practical and well-paced
- **Docs:** [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) — use as a lookup, not a read-through
- **Video:** Amigoscode — Spring Boot Full Course (YouTube)

### ✅ Phase 2 Milestones

- [ ] Build a REST API with full CRUD for a resource (e.g., a bookstore or task manager)
- [ ] Write unit tests for your service layer with Mockito
- [ ] Understand how dependency injection works and why it matters

---

## Phase 3 — Databases & Data Access (Weeks 8–10)

**Why:** Backend = data. You need to be fluent in SQL and know how Java talks to databases. This is one of your stated focus areas, so we go deeper here.

### What to Learn

- **SQL fundamentals** — joins, subqueries, indexing, transactions, normalization (if rusty)
- **Spring Data JPA / Hibernate:**
  - Entity mapping (`@Entity`, `@Table`, `@Column`, `@Id`)
  - Relationships (`@OneToMany`, `@ManyToOne`, `@ManyToMany`)
  - Repository pattern (`JpaRepository`, custom queries with `@Query`)
  - Lazy vs eager loading (common source of bugs — learn this well)
  - Database migrations with **Flyway** or **Liquibase**
- **Connection pooling** — HikariCP (default in Spring Boot)
- **NoSQL basics** — MongoDB or Redis as a cache layer (know when SQL vs NoSQL)
- **Query performance** — `EXPLAIN ANALYZE`, indexing strategies, N+1 problem

### Practical Project

Extend your Phase 2 REST API:

- Add a PostgreSQL database (use Docker to run it locally)
- Model 3–4 related entities (e.g., Users → Orders → Products)
- Implement pagination, sorting, and filtering
- Add a Redis cache for frequently-read data

### Resources

- **SQL practice:** [SQLZoo](https://sqlzoo.net/) or [Mode SQL Tutorial](https://mode.com/sql-tutorial/)
- **Book:** *High-Performance Java Persistence* by Vlad Mihalcea (advanced, but excellent for JPA/Hibernate)
- **Video:** Amigoscode — Spring Data JPA course

### ✅ Phase 3 Milestones

- [ ] Write complex SQL queries (multi-join, aggregation, window functions)
- [ ] Model a relational schema with 4+ entities and proper relationships
- [ ] Understand and fix an N+1 query problem
- [ ] Run PostgreSQL + Redis in Docker containers

---

## Phase 4 — Microservices & API Design (Weeks 11–14)

**Why:** This is the architecture modern Java shops use. You'll learn how to break a monolith into services and make them communicate reliably.

### What to Learn

- **RESTful API design best practices** — resource naming, status codes, HATEOAS, versioning
- **API documentation** — OpenAPI / Swagger (`springdoc-openapi`)
- **Authentication & authorization** — Spring Security + JWT (you know JWTs from the frontend — now implement the issuer side)
- **Microservice patterns:**
  - Service decomposition — bounded contexts (from Domain-Driven Design)
  - Synchronous communication — REST + OpenFeign / RestClient
  - Asynchronous communication — message queues with **RabbitMQ** or **Apache Kafka**
  - API Gateway pattern (Spring Cloud Gateway)
  - Service discovery (Eureka or Consul — understand the concept, don't memorize config)
  - Circuit breaker (Resilience4j) — graceful failure handling
  - Distributed tracing (Micrometer + Zipkin)
- **Docker** — Dockerize your Spring Boot apps, `docker-compose` for multi-service local dev
- **Configuration management** — externalized config, Spring Cloud Config or environment variables

### Practical Project — "Mini E-Commerce System"

Build 2–3 microservices that work together:

| Service | Responsibility |
|---|---|
| **User Service** | Registration, auth, JWT issuance |
| **Product Service** | Product catalog CRUD |
| **Order Service** | Order placement, communicates with Product Service |

Connect them with REST calls + one async event (e.g., "order placed" event via RabbitMQ). Dockerize everything with `docker-compose`.

### Resources

- **Book:** *Building Microservices* by Sam Newman (architecture mindset)
- **Course:** [Microservices with Spring Boot 3](https://www.udemy.com/course/microservices-with-spring-boot-and-spring-cloud/) by in28Minutes (Udemy)
- **Docs:** [Spring Cloud](https://spring.io/projects/spring-cloud) official docs

### ✅ Phase 4 Milestones

- [ ] Implement JWT-based auth end-to-end
- [ ] Two services communicate via REST and via a message queue
- [ ] Everything runs with `docker-compose up`
- [ ] API documented with Swagger UI

---

## Phase 5 — System Design & Production Readiness (Weeks 15–18)

**Why:** Interviews test system design, and real jobs demand production awareness. This phase bridges the gap from "I can code" to "I can design and ship."

### System Design Concepts

- **Scaling patterns** — horizontal vs vertical, load balancing, stateless services
- **Caching strategies** — cache-aside, write-through, TTL policies
- **Database scaling** — read replicas, sharding, connection pooling
- **CAP theorem & consistency models** — eventual consistency, strong consistency
- **Rate limiting & throttling** — token bucket, sliding window
- **Event-driven architecture** — event sourcing, CQRS (understand concepts, don't over-engineer)
- **Observability** — logging (SLF4J + Logback), metrics (Micrometer + Prometheus), tracing (Zipkin)
- **CI/CD basics** — GitHub Actions or Jenkins pipeline for build → test → deploy

### Interview Preparation

- **System design practice:** Design a URL shortener, a notification system, a chat system — practice talking through tradeoffs aloud
- **Coding interviews:** LeetCode Medium problems in Java (aim for 30–50 solved)
- **Spring Boot interview topics:** Bean lifecycle, scopes, `@Transactional` behavior, Spring profiles, actuator endpoints
- **Behavioral:** Prepare your "switching from frontend to backend" story — frame it as expanding your impact, not running away from frontend

### Resources

- **System Design:** *Designing Data-Intensive Applications* by Martin Kleppmann (THE book)
- **System Design interviews:** [ByteByteGo](https://bytebytego.com/) by Alex Xu — visual, concise
- **LeetCode:** Focus on arrays, hashmaps, trees, graphs, and dynamic programming
- **Mock interviews:** [Pramp](https://www.pramp.com/) (free peer mock interviews)

### ✅ Phase 5 Milestones

- [ ] Whiteboard-design 3 systems (URL shortener, notification service, e-commerce) with tradeoff discussion
- [ ] Solve 30+ LeetCode Medium problems in Java
- [ ] Add logging, health checks, and metrics to your microservices project
- [ ] Set up a basic CI/CD pipeline (GitHub Actions → build → test → Docker image)

---

## Phase 6 — Portfolio & Job Search (Weeks 19–24)

### Portfolio Project (Your "Capstone")

Take your mini e-commerce system and polish it into a showcase project:

- Clean code with proper layering (Controller → Service → Repository)
- Comprehensive README with architecture diagram
- Unit + integration tests (aim for 70%+ coverage)
- Dockerized with `docker-compose`
- CI/CD pipeline
- Swagger documentation
- At least one async communication pattern (Kafka or RabbitMQ)

### GitHub Profile Checklist

- [ ] Pinned capstone project with a professional README
- [ ] 2–3 smaller Spring Boot projects showing different skills
- [ ] Consistent commit history (shows discipline)
- [ ] Clean code — imagine a senior engineer reviewing every file

### Resume Tips for Career Switchers

- Lead with skills and projects, not job titles
- Highlight transferable experience: "Built and consumed RESTful APIs", "Worked with CI/CD pipelines", "Collaborated in agile teams"
- Quantify frontend achievements but connect them to backend awareness
- List your capstone project with its tech stack prominently

### Where to Apply

- Target companies that value full-stack awareness (your frontend skills are a bonus, not a liability)
- Look for "Backend Engineer" or "Software Engineer — Java" roles
- Don't skip roles that say "Spring Boot experience preferred" — your project portfolio demonstrates this
- Consider contract or mid-level roles as an entry point

---

## Weekly Schedule Template (15 hrs/week)

| Day | Time | Activity |
|---|---|---|
| **Mon** | 2 hrs | Theory / course videos |
| **Tue** | 2 hrs | Hands-on coding (project work) |
| **Wed** | 2 hrs | Theory / reading |
| **Thu** | 3 hrs | Hands-on coding (project work) |
| **Fri** | 1 hr | LeetCode practice (1–2 problems) |
| **Sat** | 3 hrs | Deep work — project building or system design |
| **Sun** | 2 hrs | Review, notes, plan next week |

---

## Essential Tools to Set Up (Day 1)

| Tool | Purpose |
|---|---|
| **IntelliJ IDEA** (Community Edition) | The standard Java IDE — far superior to VS Code for Java |
| **Docker Desktop** | Run databases, message queues, and your services locally |
| **Postman / Bruno** | API testing (you likely know this already) |
| **DBeaver** | Database GUI client |
| **Git** | Version control (you know this — just keep using it) |
| **SDKMan** | Manage Java versions (like `nvm` for Node) |

---

## Key Mindset Shifts: JS/TS → Java

1. **Verbosity is a feature, not a bug.** Java's explicitness makes large codebases navigable. Embrace it.
2. **Compilation catches bugs.** You'll miss fewer runtime surprises than in JS. Trust the compiler.
3. **OOP is the default paradigm.** Java loves classes, interfaces, and design patterns. Functional style exists (Streams) but OOP is king.
4. **The ecosystem is mature and stable.** Libraries don't break every 6 months. Spring Boot versions are well-supported for years.
5. **Concurrency is explicit.** No event loop magic — you manage threads (or let Spring/virtual threads handle it).

---

*Good luck on your backend journey! Your frontend experience gives you a unique edge — you understand the full picture of how users interact with the systems you'll be building.*
