# Integration Tests Rewrite — Design Spec

**Date:** 2026-04-09
**Branch:** `feature/integration`
**Status:** Approved (pending user review of written spec)

## Goal

Rewrite ALL integration tests of the DnD Platform (12 services, ~120+ test files) to follow the standards documented in the updated `~/.claude/skills/integration-tests/SKILL.md`. The current tests work but predate the skill update and lack: GWT comments, random payload templates, builder pattern, `expectedRowCount`, `where` clauses on `@DeleteEntities`, static imports, and consistent annotation ordering.

## Scope

**In scope:**
- All files matching `services/*/*/src/test/java/integration/**/*IntegrationTest.java`
- Extension of `services/common-test/` to add `@InjectRandom` (field) and `@NamedParam`
- Fix of product code only when triggered by an `anyOf(...)` workaround that reveals a real REST inconsistency, and only after explicit user approval per case

**Out of scope:**
- Unit tests
- Test entity providers (kept as-is unless touched by an integration test rewrite)
- `application.properties` of test profile (unless required to support the new utilities)
- Production code in general (see `anyOf` exception above)
- pom.xml of services (only `common-test/pom.xml` may be touched if required by new utilities)

## Decisions Made During Brainstorming

| # | Question | Decision |
|---|---|---|
| 1 | Extend `common-test` or use only existing utilities? | **Extend.** Add `@InjectRandom` (field-level) and `@NamedParam`. Aligns project to skill standard. |
| 2 | How to handle `anyOf(200, 201)` / `anyOf(400, 401)` workarounds? | Annotate with `// FIXME(integration-tests-rewrite): <reason>`, track in this doc, review case-by-case at the end with possible product fix (user approval per case). |
| 3 | Random data scope? | **Random** for happy-path data where exact value doesn't matter. **Hardcoded with `// hardcoded: <reason>` comment** when exercising specific validation, fixture-matching, or value-driven business rule. |
| 4 | Payload style: text-block JSON vs ViewModel? | **ViewModel + Builder pattern + `jsonb.toJson(...)`** (matches USB reference projects). Text-block JSON only for tests that intentionally send malformed/extra-field bodies. |
| 5 | Execution strategy: parallel vs sequential? | **Sequential, service by service.** Maximum control and consistency over speed. Use `superpowers:executing-plans` and `superpowers:verification-before-completion` as gates. |

---

## Section 1 — `common-test` Extension

**New file: `services/common-test/src/main/java/com/dndplatform/common/test/InjectRandom.java`**
```java
package com.dndplatform.common.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandom {}
```

**New file: `services/common-test/src/main/java/com/dndplatform/test/entity/NamedParam.java`**
```java
package com.dndplatform.test.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedParam {
    /** Name of the JPQL parameter. Default = field name. */
    String value() default "";
}
```

**Modify `RandomExtension.java`** — implement `BeforeEachCallback` in addition to `ParameterResolver`. New responsibility: scan the test instance for `@InjectRandom`-annotated fields, generate a random value via `Instancio.create(field.getGenericType())` and assign via reflection (set accessible). Existing `ParameterResolver` behavior preserved unchanged for `@Random` parameters.

**Modify `DeleteEntities.java`** — add `String where() default "";` attribute alongside the existing `from` and `expectedRowCount`.

**Modify `DeleteEntitiesExtension.java`** — when `where != ""`, build the JPQL as `DELETE FROM <Entity> e WHERE <where>`, then for each `:paramName` in the `where` clause, look up a `@NamedParam`-annotated field on the test instance with matching name (or `value()` override) and bind it via `query.setParameter(...)`. Otherwise current behavior is preserved (full table delete via JPQL).

**New unit tests** under `services/common-test/src/test/java/com/dndplatform/common/test/`:
- `RandomExtensionFieldInjectionTest` — verify `@InjectRandom` populates fields
- `DeleteEntitiesExtensionWhereClauseTest` — verify `where` + `@NamedParam` bindings

Build gate before touching any service:
```bash
mvn -pl services/common-test test
```
Must pass.

---

## Section 2 — Code Standards

### Class annotation order (fixed)

```java
@QuarkusTest
@QuarkusTestResource(WireMockResource1.class)         // optional, repeatable
@QuarkusTestResource(WireMockResource2.class)
@Transactional                                         // only if test reads/writes DB
@ExtendWith({
    RandomExtension.class,
    PrepareEntitiesExtension.class,
    DeleteEntitiesExtension.class
})
@PrepareEntities(XEntityProvider.class)               // optional, only if class-level seeding
class FooIntegrationTest {
```

`@ExtendWith` includes only the extensions actually needed by the test class. `RandomExtension` always first.

### Field order (fixed)

1. `@Inject` for CDI beans (`Jsonb jsonb`, `EntityManager em`, …)
2. `@InjectMockServer(...)` for WireMock
3. `@InjectRandom` payload templates
4. `@NamedParam` + `@InjectRandom` JPQL parameters

### Imports — always static, never inline FQN

```java
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
```

**Forbidden:** `org.hamcrest.Matchers.equalTo(...)` written inline in the test body. Always lift as static import.

### Test method structure

```java
@Test
@TestSecurity(user = "1", roles = "PLAYER")
@DeleteEntities(from = UserEntity.class, where = "e.username = :username", expectedRowCount = 1)
void shouldRegisterNewUser() {
    // given
    var request = UserRegisterViewModelBuilder.from(payloadTemplate)
            .withUsername(username)
            .withPassword("MyPassword1") // hardcoded: passes @Pattern complexity
            .build();

    // when
    var response = given()
            .contentType(JSON)
            .body(jsonb.toJson(request))
    .when()
            .post("/users")
    .then()
            .statusCode(201)
            .extract().as(UserViewModel.class);

    // then
    assertThat(response.id()).isNotNull();
    assertThat(response.username()).isEqualTo(username);
}
```

### Naming

- `should<Behavior>` for happy path: `shouldRegisterNewUser`, `shouldFindUserById`
- `shouldFail<Reason>` for error: `shouldFailWhenUsernameIsBlank`, `shouldFailWhenNotAuthenticated`
- **No** `shouldReturn400` / `shouldReturn401` — describe the behaviour, not the status code

### GWT comments

Always lowercase: `// given`, `// when`, `// then`. Combine `// when / then` when the action is also the assertion (e.g. status-code-only checks via `.statusCode(400)`).

### `hardcoded:` comments

Mandatory above any value not coming from a random template, explaining **why** it is hardcoded. Examples:
- `// hardcoded: passes @Pattern complexity`
- `// hardcoded: matches @PrepareEntities seed`
- `// hardcoded: triggers @NotBlank validation`
- `// hardcoded: matches WireMock stub`

### `anyOf` workaround

Each surviving `anyOf(...)` must be annotated with `// FIXME(integration-tests-rewrite): <product bug description>` and added to the FIXME tracker (this doc).

---

## Section 3 — Test Categories

Each test maps to one of five categories. Categorization is a per-file decision during execution.

### Category A — Input validation (no DB, no auth)

Tests sending malformed payloads expecting 4xx. No `@Transactional`, no `@DeleteEntities`, no seeding.

```java
@QuarkusTest
@ExtendWith(RandomExtension.class)
class UserRegisterValidationIntegrationTest {

    @Inject
    Jsonb jsonb;

    @InjectRandom
    private UserRegisterViewModel payloadTemplate;

    @Test
    void shouldFailWhenUsernameIsBlank() {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername("") // hardcoded: triggers @NotBlank
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(jsonb.toJson(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }
}
```

### Category B — CRUD happy path with DB

Creates/reads/updates/deletes an entity. `@Transactional`, `@DeleteEntities` with `where` + `expectedRowCount`.

```java
@QuarkusTest
@Transactional
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserRegisterIntegrationTest {

    @Inject
    Jsonb jsonb;

    @InjectRandom
    private UserRegisterViewModel payloadTemplate;

    // The same field acts as both:
    //   - the value injected into the payload via .withUsername(username)
    //   - the JPQL :username parameter for @DeleteEntities cleanup
    @NamedParam
    @InjectRandom
    private String username;

    @Test
    @DeleteEntities(from = UserEntity.class, where = "e.username = :username", expectedRowCount = 1)
    void shouldRegisterNewUser() {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername(username)
                .withPassword("MyPassword1") // hardcoded: passes @Pattern complexity
                .withEmail("user@example.com") // hardcoded: passes @Email
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(jsonb.toJson(request))
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.role()).isEqualTo("PLAYER");
        assertThat(response.active()).isTrue();
    }
}
```

### Category C — Conflict / business rule (requires seed)

Test requiring a pre-existing entity to verify a conflict (409, 403, 404).

```java
@QuarkusTest
@Transactional
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
@PrepareEntities(UserEntityProvider.class)
class UserRegisterConflictIntegrationTest {

    @Inject
    Jsonb jsonb;

    @InjectRandom
    private UserRegisterViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldFailWhenUsernameAlreadyExists() {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername(UserEntityProvider.USERNAME) // hardcoded: matches seed
                .withEmail("different@example.com") // hardcoded: isolates the conflict to username
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(jsonb.toJson(request))
        .when()
                .post("/users")
        .then()
                .statusCode(409);
    }
}
```

### Category D — JWT-protected endpoint

Same A/B/C structure, plus `@TestSecurity(user = "1", roles = "PLAYER")` on the individual method. A dedicated `shouldFailWhenNotAuthenticated` test WITHOUT `@TestSecurity` verifies the 401.

```java
@Test
@TestSecurity(user = "1", roles = "PLAYER")
@DeleteEntities(from = CampaignEntity.class, where = "e.id = :campaignId", expectedRowCount = 1)
void shouldCreateCampaign() { ... }

@Test
void shouldFailWhenNotAuthenticated() {
    given()
            .contentType(JSON)
            .body(jsonb.toJson(payloadTemplate))
    .when()
            .post("/campaigns")
    .then()
            .statusCode(401);
}
```

### Category E — WireMock for external service

Typical for `auth-service` calling `user-service`, or `document-qa-service` calling LLM.

```java
@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@Transactional
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CreateLoginTokensIntegrationTest {

    @Inject
    Jsonb jsonb;

    @InjectMockServer(UserServiceWireMockResource.class)
    private WireMockServer userServiceServer;

    @InjectRandom
    private CreateLoginTokensViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldCreateLoginTokens() {
        // given
        var request = CreateLoginTokensViewModelBuilder.from(payloadTemplate)
                .withUsername("gandalf") // hardcoded: matches WireMock stub
                .withPassword("YouShallNotPass1") // hardcoded: matches stub
                .build();

        userServiceServer.givenThat(
                get("/users/by-username/gandalf")
                        .willReturn(okJson("""
                                {"id":1,"username":"gandalf","passwordHash":"...","role":"PLAYER"}
                                """)));

        // when
        var response = given()
                .contentType(JSON)
                .body(jsonb.toJson(request))
        .when()
                .post("/auth/login-tokens")
        .then()
                .statusCode(201)
                .extract().as(LoginTokensViewModel.class);

        // then
        assertThat(response.accessToken()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();
        assertThat(response.userId()).isEqualTo(1L);
    }
}
```

---

## Section 4 — Service Plan (Order, Dependencies, Gating)

### Pre-phase: `common-test` extension

Unblocks everything. Build gate: `mvn -pl services/common-test test` must pass before touching any service.

### Service order

| # | Service | ~#test | Notes |
|---|---|---|---|
| 0 | `common-test` | new utilities | Pre-phase, unblocks the rest |
| 1 | **user-service** | 7 | Smallest. **Reference implementation.** Pure CRUD. |
| 2 | auth-service | 8 | Category E (WireMock on user-service) |
| 3 | campaign-service | 24 | CRUD + JWT-heavy. High volume. |
| 4 | character-service | 3 | Multipart upload, partially blocked |
| 5 | combat-service | 11 | CRUD + JWT |
| 6 | chat-service | 6 | CRUD + JWT |
| 7 | notification-service | 3 | Email template (Category B) |
| 8 | asset-service | 2 | Document upload (multipart) |
| 9 | compendium-service | ~30 | Read-only on Flyway seed, no DB write. Mostly Category A. |
| 10 | document-qa-service | ~14 | LLM mock + ingestion. Category E heavy. |

`search-service` is intentionally excluded: it currently has zero integration tests. Adding tests for it is out of scope for this rewrite (would be a separate spec).

Each service ≥1 explicitly depends on pre-phase (service 0).

### Per-service gate (rigid)

1. Read ALL tests in the service before touching them
2. Categorize each (A/B/C/D/E) and flag `anyOf` workarounds for FIXME tracker
3. Rewrite one test at a time following Section 3 templates
4. `mvn test -pl services/<service>/<service>` — MUST be green
5. Internal code review: re-read the diff and verify adherence to Section 2 standards
6. Mark task completed → next service

### Failed-gate behaviour

If `mvn test` fails, do NOT proceed to next service. Diagnose and fix. If the fix requires changes to `common-test`, return to pre-phase, update, retest everything downstream.

### What is NOT touched

- Production code (except bug fixes from final `anyOf` review, with user approval)
- Unit tests
- Test `application.properties` (unless required to support new utilities)
- pom.xml (except `common-test/pom.xml` if new dependencies are needed)

---

## Section 5 — Verification Strategy

### Per-test verification

- Compiles without warnings
- Respects Section 2 standards (annotation order, field order, static imports, GWT comments, `hardcoded:` comments)
- Category correctly identified (A/B/C/D/E)

### Per-service verification (gate before next service)

1. **Clean build:**
   ```bash
   mvn clean test -pl services/<service>/<service>
   ```
   All tests green. Zero new `@Disabled` (existing ones with their reason are preserved).

2. **Diff review:**
   - Re-read the full diff of the service
   - No coverage regression (each original test has a counterpart in the rewrite)
   - Assertions at least as strong as before (no `statusCode(200)` only where the original also asserted on the body)

3. **Static checks:**
   - `grep "// Arrange\|// Act\|// Assert" services/<svc>/...` → 0 results
   - `grep "org.hamcrest.Matchers\." services/<svc>/...` → 0 results (all static-imported)
   - `grep "@DeleteEntities(from" services/<svc>/...` → all have `where` or an inline note explaining why not

4. **FIXME tracker updated:** every `// FIXME(integration-tests-rewrite)` added must be reported in the tracker section below.

### Global verification (after service 10)

1. **Full build:**
   ```bash
   mvn clean test
   ```
   From project root. All tests green.

2. **Coverage check (recommended):**
   - JaCoCo already configured (cf. commit `5863af5`)
   - Compare pre/post reports: per-service coverage must not drop

3. **Final `anyOf` tracker review:**
   - Walk the list line by line with the user
   - Per item: decide if it's a product bug (→ issue + fix) or wrong test expectation (→ fix test)
   - Explicit user approval per product fix

4. **`superpowers:requesting-code-review`** invoked at the end for a second opinion on the whole rewrite.

### Rollback strategy

Work happens on `feature/integration` (already active). One commit per service (12 service commits + 1 pre-phase commit + 1 anyOf fix commit). If a step breaks irreparably: `git revert` of that service's commit, no work loss.

---

## FIXME Tracker (populated during execution)

| Service | File | Line | Type | Note |
|---|---|---|---|---|
| _empty_ | | | | |

---

## Decisions Log (for future reference)

- `@InjectRandom` is project-internal (`com.dndplatform.common.test`), not the same as the parameter-level `@Random` already present. Both coexist: `@Random` for method parameters, `@InjectRandom` for test instance fields.
- `@NamedParam` defaults to the field name; the `value()` override is provided for cases where the JPQL placeholder name differs from the Java field name (avoid using it unless strictly necessary).
- `Jsonb` is used (not `ObjectMapper`) because Quarkus REST default serializer is JSON-B.
- `anyOf` workarounds are NEVER acceptable in the final state; they only survive temporarily inside the rewrite as `FIXME` markers.
