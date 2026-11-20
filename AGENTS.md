# AGENTS.md

Guidelines for agents (and contributors) working on this repository. Read this file before editing code.

## Project overview

OCPP configuration server: a Spring Boot web application used to configure charge points over the OCPP protocol (WebSocket, OCPP 1.6 and 2.0).

- Java 21, Maven 3.9.6+, Spring Boot 3.2.2, Lombok, JaCoCo, Spotless (google-java-format, GOOGLE style).
- Multi-module Maven build: `domain`, `database`, `websocket`, `service`, `coverage-report`.

## Architecture (hexagonal)

Dependency rule: outer modules depend on `domain`, never the reverse. `domain` must not depend on Spring or any other infrastructure framework.

- `domain` — the core, no framework dependencies:
  - `models`: immutable domain models as Java `record`s, with `of(...)` factories and `with...` copy methods.
  - `ports/api`: inbound ports (use-case interfaces), e.g. `ChargePointService`.
  - `ports/spi`: outbound ports (repository interfaces), e.g. `ChargePointRepository`.
  - `services`: adapters implementing the API ports on top of the SPI ports, annotated `@DomainService`.
  - `exceptions`: domain exceptions (`BadRequestException`, `NotFoundException`, ...). User-facing messages are written in French.
  - `annotations`: `@DomainService`.
- `database` — SPI adapters: JPA `entities`, Spring Data `dao`, and `adapters` implementing the SPI ports.
- `service` — API adapters: per feature package, a REST `Controller` with its `Dto` and `Mapper`; plus `security`, `configuration`, `errors`, `openapi`, and the WebSocket wiring to the domain.
- `websocket` — OCPP protocol handling (`ocpp16` / `ocpp2` message types, `tools`).
- `coverage-report` — JaCoCo aggregate report module (build tooling only, never business code).

A new feature flows: adapter (`service` or `websocket`) → API port (`domain/ports/api`) → domain service adapter (`domain/services`) → SPI port (`domain/ports/spi`) → persistence adapter (`database/adapters`).

## TDD (mandatory)

1. **Red**: write a failing test first, in the module under change, mirroring the main package layout under `src/test/java`.
2. **Green**: implement the minimal code that makes the test pass.
3. **Refactor**: clean up while keeping the tests green.

Test conventions:

- JUnit 5 + AssertJ (`assertThat`, `assertThatThrownBy`).
- Test method names: `should_<behavior>` / `should_not_<behavior>` (e.g. `should_not_save_when_charge_point_already_existing`).
- Domain unit tests use the in-memory stubs from `domain/src/test` (`ChargePointRepositoryStub`, ...) and the factories (`ChargePointFactory.dummyChargePoint()`, ...). Inject a fixed `Clock` for time-dependent code. Do not start a Spring context for domain tests.
- Run one module with `mvn -pl <module> test`; all modules with `mvn test`.

## Clean code

- Code, type and method names in English; user-facing error messages in French.
- Format with google-java-format: run `mvn spotless:apply` before committing. `mvn validate` (spotless:check) is enforced by CI.
- Public types and methods carry Javadoc; avoid inline noise comments.
- Use `record` for models and DTOs; use Lombok only where it removes real boilerplate; validate constructor arguments with `Objects.requireNonNull` (compact constructor for records).
- Keep methods small and intention-revealing; throw the exceptions from `domain/exceptions` instead of generic ones.
- Do not add a dependency unless the task requires it. Do not modify generated code or files unrelated to the task.

## Build and verification (same as CI)

CI (`.github/workflows/maven.yml`) runs on every push and pull request to `main` and `dev`:

```bash
mvn validate   # style check (spotless:check)
mvn verify     # tests + JaCoCo coverage reports
```

Run both locally before pushing. Coverage reports: per module in `<module>/target/site/jacoco/`, aggregate in `coverage-report/target/site/jacoco-aggregate/`; Codecov posts a comment on PRs.

Run the app locally with the `local` profile (in-memory H2, seeded data):

```bash
mvn package -DskipTests
SPRING_PROFILES_ACTIVE=local java -jar service/target/service-0.0.1-SNAPSHOT.jar
```

See `README.md` for details (Swagger UI, H2 console, charge point emulator in `doc/`).

## Git and pull request workflow

- Base branch is `dev` (`main` is protected). Never push directly to `dev` or `main`.
- Work on a dedicated branch: `vibe/<short-slug>-e38b99` (or the branch named by the issue).
- Commits use conventional commits: `type: subject` (`feat`, `fix`, `test`, `refactor`, `docs`, ...), one focused commit per unit of work.
- Write all work artifacts in English: commit messages, PR titles and descriptions, and issue/PR comments. Only user-facing runtime error messages are in French.
- Open a ready-for-review PR targeting `dev`, with a body containing:
  - `## Summary` — what and why;
  - `## Verification` — the exact commands run;
  - `Closes #<issue>` when the PR resolves a GitHub issue.
- Pick work from the open issues (`gh issue list --state open`); self-assign with `gh issue edit <n> --add-assignee @me`. Issue titles follow the `<story>-<type> description` pattern.
- Keep the change minimal and scoped to the issue. CI must be green before merge; a maintainer reviews and merges.
