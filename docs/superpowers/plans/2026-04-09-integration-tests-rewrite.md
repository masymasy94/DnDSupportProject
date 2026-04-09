# Integration Tests Rewrite Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
>
> **Spec:** `docs/superpowers/specs/2026-04-09-integration-tests-rewrite-design.md` — read it before starting any task. The spec defines the 5 test categories (A=validation, B=CRUD, C=conflict, D=JWT, E=WireMock), the code standards, and the FIXME tracker policy.

**Goal:** Rewrite all 117 integration tests across 10 services to follow the updated unit-tests/integration-tests skills, after extending `common-test` with `@InjectRandom` (field) and `@NamedParam`.

**Architecture:** TDD for the `common-test` extension (Phase 0), then sequential service-by-service rewrite (Phase 1–10) with a strict per-service gate (`mvn test -pl services/<svc>/<svc>` must be green before moving on). Each service produces one commit. The reference implementation is `user-service` (Phase 1) — its rewrite is the canonical pattern that later services replicate.

**Tech Stack:** Quarkus 3.31.4, JUnit 5, REST-Assured, AssertJ, Instancio (random data), Jackson `ObjectMapper` (server-side serializer), `@TestSecurity` (Quarkus JWT mock), WireMock (external service mocks), H2 (test DB).

**Maven invocation:** Maven not on PATH. Use:
```bash
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn"
```
Wherever this plan says `mvn ...`, substitute the absolute path above.

---

## Phase 0 — Extend `common-test`

### Task 0.1: Add `@InjectRandom` annotation and field-level injection in `RandomExtension`

**Files:**
- Create: `services/common-test/src/main/java/com/dndplatform/common/test/InjectRandom.java`
- Modify: `services/common-test/src/main/java/com/dndplatform/common/test/RandomExtension.java`
- Test: `services/common-test/src/test/java/com/dndplatform/common/test/RandomExtensionFieldInjectionTest.java`

- [ ] **Step 1: Write the failing test**

Create `services/common-test/src/test/java/com/dndplatform/common/test/RandomExtensionFieldInjectionTest.java`:

```java
package com.dndplatform.common.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class RandomExtensionFieldInjectionTest {

    @InjectRandom
    private String randomString;

    @InjectRandom
    private Integer randomInteger;

    @InjectRandom
    private UUID randomUuid;

    @InjectRandom
    private SamplePayload randomPayload;

    @Test
    void shouldInjectRandomStringField() {
        assertThat(randomString).isNotNull().isNotBlank();
    }

    @Test
    void shouldInjectRandomIntegerField() {
        assertThat(randomInteger).isNotNull();
    }

    @Test
    void shouldInjectRandomUuidField() {
        assertThat(randomUuid).isNotNull();
    }

    @Test
    void shouldInjectRandomComplexPayloadField() {
        assertThat(randomPayload).isNotNull();
        assertThat(randomPayload.username()).isNotNull();
        assertThat(randomPayload.email()).isNotNull();
        assertThat(randomPayload.age()).isNotNull();
    }

    record SamplePayload(String username, String email, Integer age) {}
}
```

- [ ] **Step 2: Run the test to verify it fails to compile**

```bash
mvn -pl services/common-test test -Dtest=RandomExtensionFieldInjectionTest
```

Expected: COMPILE FAIL — `cannot find symbol: class InjectRandom`.

- [ ] **Step 3: Create the `@InjectRandom` annotation**

Create `services/common-test/src/main/java/com/dndplatform/common/test/InjectRandom.java`:

```java
package com.dndplatform.common.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandom {
}
```

- [ ] **Step 4: Run the test again — now it should fail at runtime**

```bash
mvn -pl services/common-test test -Dtest=RandomExtensionFieldInjectionTest
```

Expected: COMPILE OK, RUNTIME FAIL — assertions fail because the fields are still null (extension does not yet inject them).

- [ ] **Step 5: Update `RandomExtension` to implement field injection**

Replace the entire content of `services/common-test/src/main/java/com/dndplatform/common/test/RandomExtension.java`:

```java
package com.dndplatform.common.test;

import org.instancio.Instancio;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Field;

public class RandomExtension implements ParameterResolver, BeforeEachCallback {

    @Override
    public boolean supportsParameter(ParameterContext paramCtx, ExtensionContext extCtx) {
        return paramCtx.isAnnotated(Random.class);
    }

    @Override
    public Object resolveParameter(ParameterContext paramCtx, ExtensionContext extCtx) {
        return Instancio.create(paramCtx.getParameter().getType());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var testInstance = context.getRequiredTestInstance();
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectRandom.class)) {
                    field.setAccessible(true);
                    Object value = Instancio.create(field.getType());
                    field.set(testInstance, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
```

- [ ] **Step 6: Run the test — should pass**

```bash
mvn -pl services/common-test test -Dtest=RandomExtensionFieldInjectionTest
```

Expected: PASS, 4 tests green.

- [ ] **Step 7: Run the FULL `common-test` suite to ensure no regression on the existing `@Random` parameter behaviour**

```bash
mvn -pl services/common-test test
```

Expected: PASS, all tests green.

- [ ] **Step 8: Commit**

```bash
git add services/common-test/src/main/java/com/dndplatform/common/test/InjectRandom.java \
        services/common-test/src/main/java/com/dndplatform/common/test/RandomExtension.java \
        services/common-test/src/test/java/com/dndplatform/common/test/RandomExtensionFieldInjectionTest.java
git commit -m "$(cat <<'EOF'
test(common-test): add @InjectRandom field-level injection

RandomExtension now implements BeforeEachCallback, scanning the
test instance for @InjectRandom-annotated fields and populating
each one via Instancio. Existing @Random parameter behaviour is
preserved unchanged.

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 0.2: Add `@NamedParam` annotation and `where` clause support in `@DeleteEntities`

**Files:**
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/NamedParam.java`
- Modify: `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntities.java`
- Modify: `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntitiesExtension.java`
- Test: `services/common-test/src/test/java/com/dndplatform/test/entity/DeleteEntitiesExtensionWhereClauseTest.java`

> **Note:** This task tests reflection logic in isolation, NOT the full JPA round-trip (which would require a Quarkus test bootstrap). The full integration is exercised by Phase 1 tests in `user-service`.

- [ ] **Step 1: Write the failing test (reflection-only, no JPA)**

Create `services/common-test/src/test/java/com/dndplatform/test/entity/DeleteEntitiesExtensionWhereClauseTest.java`:

```java
package com.dndplatform.test.entity;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class DeleteEntitiesExtensionWhereClauseTest {

    @NamedParam
    @InjectRandom
    private String username;

    @NamedParam("custom_name")
    @InjectRandom
    private Long entityId;

    @Test
    void namedParamShouldDefaultToFieldName() throws Exception {
        var field = getClass().getDeclaredField("username");
        var annotation = field.getAnnotation(NamedParam.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEmpty();
        assertThat(DeleteEntitiesExtension.resolveParamName(field)).isEqualTo("username");
    }

    @Test
    void namedParamShouldUseExplicitValueWhenProvided() throws Exception {
        var field = getClass().getDeclaredField("entityId");
        var annotation = field.getAnnotation(NamedParam.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo("custom_name");
        assertThat(DeleteEntitiesExtension.resolveParamName(field)).isEqualTo("custom_name");
    }

    @Test
    void shouldCollectNamedParamFieldsFromTestInstance() {
        var params = DeleteEntitiesExtension.collectNamedParams(this);

        assertThat(params).containsKeys("username", "custom_name");
        assertThat(params.get("username")).isEqualTo(username);
        assertThat(params.get("custom_name")).isEqualTo(entityId);
    }
}
```

- [ ] **Step 2: Run the test to verify it fails to compile**

```bash
mvn -pl services/common-test test -Dtest=DeleteEntitiesExtensionWhereClauseTest
```

Expected: COMPILE FAIL — `cannot find symbol: class NamedParam`, `method resolveParamName`, `method collectNamedParams`.

- [ ] **Step 3: Create `@NamedParam` annotation**

Create `services/common-test/src/main/java/com/dndplatform/test/entity/NamedParam.java`:

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

- [ ] **Step 4: Add `where()` to `@DeleteEntities`**

Replace `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntities.java`:

```java
package com.dndplatform.test.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DeleteEntitiesList.class)
public @interface DeleteEntities {
    Class<?> from();
    int expectedRowCount() default -1;
    /**
     * JPQL where clause body (without the "WHERE" keyword), referencing the
     * entity alias `e` and named parameters bound to {@link NamedParam} fields
     * on the test class. Example: {@code where = "e.username = :username"}.
     */
    String where() default "";
}
```

- [ ] **Step 5: Update `DeleteEntitiesExtension` with `where` support and helper methods**

Replace `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntitiesExtension.java`:

```java
package com.dndplatform.test.entity;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteEntitiesExtension implements AfterEachCallback {

    private static final Pattern NAMED_PARAM_PATTERN = Pattern.compile(":([a-zA-Z_][a-zA-Z0-9_]*)");

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        var method = context.getTestMethod().orElse(null);
        if (method == null) {
            return;
        }
        var annotations = method.getAnnotationsByType(DeleteEntities.class);
        if (annotations.length == 0) {
            return;
        }
        var testInstance = context.getRequiredTestInstance();
        var namedParams = collectNamedParams(testInstance);

        var em = CDI.current().select(EntityManager.class).get();
        var tx = CDI.current().select(UserTransaction.class).get();
        try {
            tx.begin();
            for (var annotation : annotations) {
                var entityName = annotation.from().getSimpleName();
                String jpql;
                if (annotation.where().isBlank()) {
                    jpql = "DELETE FROM " + entityName;
                } else {
                    jpql = "DELETE FROM " + entityName + " e WHERE " + annotation.where();
                }
                Query query = em.createQuery(jpql);
                if (!annotation.where().isBlank()) {
                    bindNamedParams(query, annotation.where(), namedParams);
                }
                int deleted = query.executeUpdate();
                if (annotation.expectedRowCount() >= 0) {
                    assertThat(deleted)
                            .as("Row count for: %s", jpql)
                            .isEqualTo(annotation.expectedRowCount());
                }
            }
            tx.commit();
        } catch (Exception e) {
            try { tx.rollback(); } catch (Exception ignored) {}
            throw new RuntimeException("Failed to delete entities", e);
        }
    }

    /**
     * Visible for testing.
     * Resolves the JPQL parameter name for a {@link NamedParam} field:
     * uses the explicit annotation value if non-empty, otherwise the field name.
     */
    static String resolveParamName(Field field) {
        var annotation = field.getAnnotation(NamedParam.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not annotated with @NamedParam");
        }
        return annotation.value().isEmpty() ? field.getName() : annotation.value();
    }

    /**
     * Visible for testing.
     * Walks the test instance class hierarchy and returns a map of
     * paramName -> currentValue for every {@link NamedParam}-annotated field.
     */
    static Map<String, Object> collectNamedParams(Object testInstance) {
        Map<String, Object> result = new HashMap<>();
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NamedParam.class)) {
                    field.setAccessible(true);
                    try {
                        result.put(resolveParamName(field), field.get(testInstance));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot read @NamedParam field " + field.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    private static void bindNamedParams(Query query, String whereClause, Map<String, Object> namedParams) {
        Matcher matcher = NAMED_PARAM_PATTERN.matcher(whereClause);
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (!namedParams.containsKey(paramName)) {
                throw new IllegalStateException(
                        "JPQL where clause references :" + paramName
                                + " but no @NamedParam field with that name exists on the test class. "
                                + "Available: " + namedParams.keySet());
            }
            query.setParameter(paramName, namedParams.get(paramName));
        }
    }
}
```

- [ ] **Step 6: Run the test — should pass**

```bash
mvn -pl services/common-test test -Dtest=DeleteEntitiesExtensionWhereClauseTest
```

Expected: PASS, 3 tests green.

- [ ] **Step 7: Run the full `common-test` suite — must remain green**

```bash
mvn -pl services/common-test test
```

Expected: PASS, all tests green (RandomExtensionFieldInjectionTest + DeleteEntitiesExtensionWhereClauseTest).

- [ ] **Step 8: Commit**

```bash
git add services/common-test/src/main/java/com/dndplatform/test/entity/NamedParam.java \
        services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntities.java \
        services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntitiesExtension.java \
        services/common-test/src/test/java/com/dndplatform/test/entity/DeleteEntitiesExtensionWhereClauseTest.java
git commit -m "$(cat <<'EOF'
test(common-test): add @NamedParam and where clause to @DeleteEntities

@DeleteEntities now accepts an optional `where` JPQL clause body (e.g.
`where = "e.username = :username"`). The clause may reference :name
placeholders bound to fields on the test class annotated with
@NamedParam. The annotation defaults to the field name and accepts an
explicit value() override.

DeleteEntitiesExtension exposes two package-visible helpers
(resolveParamName, collectNamedParams) that are unit-tested via
reflection without bootstrapping CDI/JPA. Full JPA round-trip is
exercised by the integration tests of the user-service phase.

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 0.3: Pre-phase verification and tag

- [ ] **Step 1: Build everything from the project root to make sure no service compiles broken against the changed annotations**

```bash
mvn clean install -DskipTests -pl services/common-test -am
```

Expected: BUILD SUCCESS.

- [ ] **Step 2: Tag the pre-phase commit for easy rollback**

```bash
git tag pre-phase-common-test-extended
```

---

## Phase 1 — `user-service` (Reference Implementation)

`user-service` is the canonical example. Every later phase replicates this style.

**Files in scope:**
- `services/user-service/user-service/src/test/java/integration/user/UserRegisterIntegrationTest.java`
- `services/user-service/user-service/src/test/java/integration/user/UserFindByIdIntegrationTest.java`
- `services/user-service/user-service/src/test/java/integration/user/UserFindAllIntegrationTest.java`
- `services/user-service/user-service/src/test/java/integration/user/UserSearchIntegrationTest.java`
- `services/user-service/user-service/src/test/java/integration/user/UserFindByEmailIntegrationTest.java`
- `services/user-service/user-service/src/test/java/integration/user/UserCredentialsValidateIntegrationTest.java`
- `services/user-service/user-service/src/test/java/integration/user/UserUpdatePasswordIntegrationTest.java`
- Existing helper: `services/user-service/user-service/src/test/java/integration/user/UserEntityProvider.java` (left as-is unless it must change)
- Existing builder: check for `UserRegisterViewModelBuilder` etc., generated by `@Builder` annotation processor on the record

### Task 1.1: Audit user-service tests

- [ ] **Step 1: Read every test file in `services/user-service/user-service/src/test/java/integration/user/`** and produce a written audit (in your scratchpad, not in a file). For each file record:
  - Test class name
  - Each `@Test` method name
  - Category (A=validation, B=CRUD, C=conflict, D=JWT, E=WireMock — see spec section 3)
  - Whether it currently uses hardcoded payload, text-block JSON, or anything noteworthy
  - Whether it currently has `anyOf(...)` workarounds (these become FIXME entries)

- [ ] **Step 2: Verify the existence of generated builders** for the view models the tests need:

```bash
ls services/user-service/user-service/target/generated-sources/annotations/com/dndplatform/user/view/model/vm/ 2>/dev/null
```

If the directory doesn't exist, run:
```bash
mvn -pl services/user-service/user-service compile -DskipTests
```

Expected: builders like `UserRegisterViewModelBuilder.java` exist. If not, the `@Builder` processor is not active for view-model — STOP and report.

- [ ] **Step 3: Verify that `UserEntity` exposes a `username` and `email` column** that can be queried by JPQL (so we can write `where = "e.username = :username"`):

```bash
grep -n "@Column\|String username\|String email" services/user-service/user-service-adapter-outbound/src/main/java/com/dndplatform/user/adapter/outbound/jpa/entity/UserEntity.java
```

Confirm `username` and `email` are present and use JPA naming compatible with JPQL `e.username` / `e.email`.

### Task 1.2: Rewrite `UserRegisterIntegrationTest` (Categories B + C)

**Files:**
- Modify (full rewrite): `services/user-service/user-service/src/test/java/integration/user/UserRegisterIntegrationTest.java`

> **Why this test first:** it's the most complex of user-service (CRUD happy-path + multiple validation cases + two conflict cases). Once it works, the rest are simpler variants.

- [ ] **Step 1: Replace the entire content of the file with the rewritten version**

```java
package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.NamedParam;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserRegisterViewModelBuilder;
import com.dndplatform.user.view.model.vm.UserViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Transactional
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserRegisterIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private UserRegisterViewModel payloadTemplate;

    // Same field acts as:
    //   - value injected into payload via .withUsername(username)
    //   - JPQL :username parameter for @DeleteEntities cleanup
    @NamedParam
    @InjectRandom
    private String username;

    @Test
    @DeleteEntities(from = UserEntity.class, where = "e.username = :username", expectedRowCount = 1)
    void shouldRegisterNewUser() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername(username)
                .withEmail("user-" + username + "@example.com") // hardcoded shape: must satisfy @Email
                .withPassword("MyPassword1") // hardcoded: must satisfy password complexity
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.email()).isEqualTo("user-" + username + "@example.com");
        assertThat(response.role()).isEqualTo("PLAYER");
        assertThat(response.active()).isTrue();
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void shouldFailWhenUsernameIsBlank() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername("") // hardcoded: triggers @NotBlank
                .withEmail("valid@email.com") // hardcoded: isolate the failure to username
                .withPassword("MyPassword1") // hardcoded: passes password complexity
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenUsernameIsTooShort() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername("ab") // hardcoded: below @Size(min) constraint
                .withEmail("valid@email.com") // hardcoded: isolate failure to username length
                .withPassword("MyPassword1") // hardcoded: passes password complexity
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenEmailIsInvalid() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername(username)
                .withEmail("not-an-email") // hardcoded: triggers @Email validation
                .withPassword("MyPassword1") // hardcoded: passes password complexity
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenPasswordIsBlank() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername(username)
                .withEmail("valid@email.com") // hardcoded: isolate failure to password
                .withPassword("") // hardcoded: triggers @NotBlank
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenUsernameHasInvalidChars() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername("invalid user!") // hardcoded: contains space and ! to fail @Pattern
                .withEmail("valid@email.com") // hardcoded: isolate failure to username pattern
                .withPassword("MyPassword1") // hardcoded: passes password complexity
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFailWhenUsernameAlreadyExists() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername(UserEntityProvider.USERNAME) // hardcoded: matches seeded entity
                .withEmail("different@email.com") // hardcoded: isolate the conflict to username
                .withPassword("MyPassword1") // hardcoded: passes password complexity
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(409);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFailWhenEmailAlreadyExists() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.from(payloadTemplate)
                .withUsername("differentuser") // hardcoded: must differ from seeded username
                .withEmail(UserEntityProvider.EMAIL) // hardcoded: matches seeded entity
                .withPassword("MyPassword1") // hardcoded: passes password complexity
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(409);
    }
}
```

> **Important:** the original test asserted `statusCode(200)` for `shouldRegisterNewUser`. The spec says naming should focus on behaviour (`shouldFailWhen…`, not `shouldReturn400…`) but the actual product status code is preserved exactly (200 here, not 201, because that's what `user-service` currently returns). If the original used 200, keep 200. Do NOT silently change a status code — that's a product change that requires the FIXME-tracker walkthrough at the end.

- [ ] **Step 2: Run only this test class**

```bash
mvn -pl services/user-service/user-service test -Dtest=UserRegisterIntegrationTest
```

Expected: PASS, 8 tests green.

If a test fails because the random `username` contains characters that violate the username pattern (Instancio strings can include any char), open the file and constrain the random generation by overriding to a deterministic but unique value (UUID-based):

```java
@Test
@DeleteEntities(from = UserEntity.class, where = "e.username = :username", expectedRowCount = 1)
void shouldRegisterNewUser() throws JsonProcessingException {
    // given
    var safeUsername = "user" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    username = safeUsername; // override the @InjectRandom value with one that satisfies the @Pattern
    var request = ...
            .withUsername(safeUsername)
            ...
}
```

This is a documented escape hatch for tests where the random format collides with a strict `@Pattern` validator.

- [ ] **Step 3: Commit (snapshot — service not yet complete)**

```bash
git add services/user-service/user-service/src/test/java/integration/user/UserRegisterIntegrationTest.java
git commit -m "test(user-service): rewrite UserRegisterIntegrationTest

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

> **Per-test commits during the reference phase** are intentional: they make rollback granular and the diff readable. From Phase 2 onward we go back to one commit per service.

### Task 1.3: Rewrite `UserFindByIdIntegrationTest` (Category B with read)

**Files:**
- Modify (full rewrite): `services/user-service/user-service/src/test/java/integration/user/UserFindByIdIntegrationTest.java`

- [ ] **Step 1: Read the original to identify the existing test methods, the seed strategy, and assertions**

```bash
cat services/user-service/user-service/src/test/java/integration/user/UserFindByIdIntegrationTest.java
```

Note the methods and their original behaviours.

- [ ] **Step 2: Apply the rewrite following the spec templates**

Use Section 3 Categories B (read happy path) and B-error (404). Skeleton:

```java
package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Transactional
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
@PrepareEntities(UserEntityProvider.class)
class UserFindByIdIntegrationTest {

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserById() {
        // when
        var response = given()
        .when()
                .get("/users/{id}", UserEntityProvider.ID) // hardcoded: matches seeded entity ID
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.id()).isEqualTo(UserEntityProvider.ID);
        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        // when / then
        given()
        .when()
                .get("/users/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }
}
```

Adjust to match the methods that actually exist in the original (don't drop coverage). If the original asserted on more fields, keep those assertions. If it had a `shouldReturn400WhenIdIsNegative` test, port it as `shouldFailWhenIdIsNegative`.

- [ ] **Step 3: Run the test**

```bash
mvn -pl services/user-service/user-service test -Dtest=UserFindByIdIntegrationTest
```

Expected: PASS (number of tests = number from the original).

- [ ] **Step 4: Commit**

```bash
git add services/user-service/user-service/src/test/java/integration/user/UserFindByIdIntegrationTest.java
git commit -m "test(user-service): rewrite UserFindByIdIntegrationTest

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

### Task 1.4: Rewrite the remaining 5 user-service tests

Follow the procedure of Task 1.3 (read original → apply spec template → run → commit) for each of (5 files):

- `UserFindAllIntegrationTest.java`
- `UserSearchIntegrationTest.java`
- `UserFindByEmailIntegrationTest.java`
- `UserCredentialsValidateIntegrationTest.java`
- `UserUpdatePasswordIntegrationTest.java`

Per file:

- [ ] **Step (per file) 1:** Read the original, list methods and categories.
- [ ] **Step (per file) 2:** Rewrite using the matching spec template (A, B, or C). Annotate hardcoded values with `// hardcoded: <reason>`.
- [ ] **Step (per file) 3:** Run `mvn -pl services/user-service/user-service test -Dtest=<ClassName>`.
- [ ] **Step (per file) 4:** Commit `test(user-service): rewrite <ClassName>`.

If any rewrite reveals an `anyOf(...)` in the original (e.g. `anyOf(equalTo(200), equalTo(201))`), preserve the loosest behaviour with a `// FIXME(integration-tests-rewrite): <bug>` comment AND record an entry in the FIXME tracker section of the spec doc:

```bash
# Open the spec, append a row to the FIXME Tracker table at the bottom
```

### Task 1.5: User-service final verification

- [ ] **Step 1: Run the full user-service integration test suite**

```bash
mvn -pl services/user-service/user-service test -Dtest='*IntegrationTest'
```

Expected: PASS, 7 test classes green, total ~25 test methods (will vary).

- [ ] **Step 2: Run static checks**

```bash
grep -rn "// Arrange\|// Act\|// Assert" services/user-service/user-service/src/test/java/integration/ ; echo "exit=$?"
```
Expected: no matches (`exit=1` from grep).

```bash
grep -rn "org.hamcrest.Matchers\." services/user-service/user-service/src/test/java/integration/ ; echo "exit=$?"
```
Expected: no matches (`exit=1`).

```bash
grep -rn "@DeleteEntities(from" services/user-service/user-service/src/test/java/integration/
```
Expected: every `@DeleteEntities` either has `where = ...` or is on a class-level cleanup test where a full-table delete is acceptable (e.g. cleanup after `@PrepareEntities` seed).

- [ ] **Step 3: Diff review**

```bash
git log --oneline pre-phase-common-test-extended..HEAD -- services/user-service/
git diff pre-phase-common-test-extended..HEAD -- services/user-service/
```

Re-read the diff. Check that:
- Every original test method has a counterpart (no coverage regression)
- Every hardcoded value has a `hardcoded:` comment
- Every category is correctly chosen
- No `// Arrange/Act/Assert` survivors
- All payloads serialized via `objectMapper.writeValueAsString(...)`

If anything is off, fix it and add a follow-up commit before moving on.

- [ ] **Step 4: Tag service completion**

```bash
git tag phase1-user-service-complete
```

---

## Phase 2 — `notification-service`

**Files in scope:**
- `services/notification-service/notification-service/src/test/java/integration/email/CreateEmailTemplateIntegrationTest.java`
- `services/notification-service/notification-service/src/test/java/integration/email/GetAllEmailTemplatesIntegrationTest.java`
- `services/notification-service/notification-service/src/test/java/integration/email/SyncSendEmailIntegrationTest.java`
- Existing helper(s) in `integration/email/`

### Task 2.1: Audit notification-service tests

- [ ] **Step 1:** For each file in `services/notification-service/notification-service/src/test/java/integration/email/`, read it, classify it (A/B/C/D/E), record any `anyOf` workarounds.

- [ ] **Step 2: Verify builders are generated**

```bash
mvn -pl services/notification-service/notification-service compile -DskipTests
```
Then check `target/generated-sources/annotations/` for the relevant `*Builder` classes.

### Task 2.2: Rewrite `CreateEmailTemplateIntegrationTest`

**Files:**
- Modify (full rewrite): `services/notification-service/notification-service/src/test/java/integration/email/CreateEmailTemplateIntegrationTest.java`

- [ ] **Step 1:** Read the original.
- [ ] **Step 2:** Rewrite using spec section 3 Category B/C templates. Include `@InjectRandom` template, `@NamedParam` for cleanup parameters where applicable, `objectMapper.writeValueAsString(...)`, GWT comments, `hardcoded:` annotations.
- [ ] **Step 3:** Run:
```bash
mvn -pl services/notification-service/notification-service test -Dtest=CreateEmailTemplateIntegrationTest
```
Expected: PASS.
- [ ] **Step 4:** Defer commit to end-of-service (Task 2.5).

### Task 2.3: Rewrite `GetAllEmailTemplatesIntegrationTest`

- [ ] **Step 1:** Read original.
- [ ] **Step 2:** Rewrite using Category B (read with `@PrepareEntities`).
- [ ] **Step 3:** Run:
```bash
mvn -pl services/notification-service/notification-service test -Dtest=GetAllEmailTemplatesIntegrationTest
```
Expected: PASS.
- [ ] **Step 4:** Defer commit to Task 2.5.

### Task 2.4: Rewrite `SyncSendEmailIntegrationTest`

- [ ] **Step 1:** Read original. This may be Category E (mock email transport) or B (write to DB log).
- [ ] **Step 2:** Rewrite. If it relies on `quarkus.mailer.mock=true`, keep that property; do not introduce real SMTP. Note the existing setup.
- [ ] **Step 3:** Run:
```bash
mvn -pl services/notification-service/notification-service test -Dtest=SyncSendEmailIntegrationTest
```
Expected: PASS.
- [ ] **Step 4:** Defer commit to Task 2.5.

### Task 2.5: Notification-service verification + commit

- [ ] **Step 1: Full service suite**

```bash
mvn -pl services/notification-service/notification-service test -Dtest='*IntegrationTest'
```
Expected: PASS, 3 classes green.

- [ ] **Step 2: Static checks**

```bash
grep -rn "// Arrange\|// Act\|// Assert\|org.hamcrest.Matchers\." services/notification-service/notification-service/src/test/java/integration/ ; echo "exit=$?"
```
Expected: `exit=1`.

- [ ] **Step 3: Single service commit**

```bash
git add services/notification-service/notification-service/src/test/java/integration/
git commit -m "$(cat <<'EOF'
test(notification-service): rewrite all integration tests

Aligns with the integration-tests skill standards: GWT comments,
@InjectRandom payload templates, ObjectMapper serialization,
@DeleteEntities with where clauses, hardcoded value annotations.

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>
EOF
)"
git tag phase2-notification-service-complete
```

---

## Phase 3 — `asset-service`

**Files in scope:** 5 files in `services/asset-service/asset-service/src/test/java/integration/document/`

### Task 3.1: Audit

- [ ] **Step 1:** Read every file, classify, record `anyOf`.
- [ ] **Step 2:** Document upload tests use multipart. They cannot use `objectMapper.writeValueAsString` for the body; they use REST-Assured `.multiPart(...)` instead. Note which tests are multipart and which are JSON.

### Task 3.2: Rewrite `DocumentUploadBatchIntegrationTest` (multipart special)

**Files:**
- Modify: `services/asset-service/asset-service/src/test/java/integration/document/DocumentUploadBatchIntegrationTest.java`

- [ ] **Step 1:** Read original.
- [ ] **Step 2:** Rewrite. Multipart pattern:

```java
@Test
@TestSecurity(user = "1", roles = "PLAYER")
@DeleteEntities(from = DocumentEntity.class, where = "e.userId = 1", expectedRowCount = 1)
void shouldUploadDocument() {
    // given
    byte[] fileContent = "sample pdf content".getBytes(); // hardcoded: minimal valid payload

    // when
    var response = given()
            .multiPart("file", "doc.pdf", fileContent, "application/pdf")
    .when()
            .post("/documents/upload-batch")
    .then()
            .statusCode(200)
            .extract().as(UploadResultViewModel.class);

    // then
    assertThat(response.uploadedCount()).isEqualTo(1);
}
```

> **No `objectMapper` for multipart bodies** — REST-Assured's `multiPart(...)` builds the body itself.

- [ ] **Step 3:** Run:
```bash
mvn -pl services/asset-service/asset-service test -Dtest=DocumentUploadBatchIntegrationTest
```
Expected: PASS.
- [ ] **Step 4:** Defer commit to Task 3.4.

### Task 3.3: Rewrite the remaining 4 asset-service tests

For each of:
- `DocumentDeleteIntegrationTest`
- `DocumentDownloadIntegrationTest`
- `DocumentListIntegrationTest`
- `DocumentUploadIntegrationTest`

Per file:
- [ ] Read original
- [ ] Identify category (most are B + D)
- [ ] Apply matching template
- [ ] Run `mvn -pl services/asset-service/asset-service test -Dtest=<ClassName>` — expect PASS
- [ ] Defer commit to Task 3.4

### Task 3.4: Asset-service verification + commit

- [ ] **Step 1:**
```bash
mvn -pl services/asset-service/asset-service test -Dtest='*IntegrationTest'
```
Expected: PASS, 5 classes green.

- [ ] **Step 2:** Static checks (same grep as before).

- [ ] **Step 3:** Commit and tag:
```bash
git add services/asset-service/asset-service/src/test/java/integration/
git commit -m "test(asset-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase3-asset-service-complete
```

---

## Phase 4 — `character-service`

**Files in scope:** 5 files in `services/character-service/character-service/src/test/java/integration/character/`

### Task 4.1: Audit

- [ ] **Step 1:** Read every file. `CharacterImportSheetIntegrationTest.java` is multipart-based and very thin (only 401/400 cases) — that's fine, it's Category A + D. Also note which tests are `@Disabled` and preserve their disable comments verbatim.

### Task 4.2: Rewrite `CharacterImportSheetIntegrationTest`

- [ ] **Step 1:** Read original.
- [ ] **Step 2:** Rewrite as Category A + D (multipart). Example for the existing 401 case:

```java
@Test
void shouldFailWhenNotAuthenticated() {
    given()
            .contentType("multipart/form-data")
    .when()
            .post("/characters/import-sheet")
    .then()
            .statusCode(401);
}
```

For the existing test that uses `anyOf(400, 415)`: the spec says these are FIXME entries — preserve loosest behaviour with FIXME comment, add to tracker. Example:

```java
@Test
@TestSecurity(user = "1", roles = "PLAYER")
void shouldFailWhenFileIsMissing() {
    // FIXME(integration-tests-rewrite): product returns either 400 or 415
    // depending on whether the multipart parser short-circuits. Decide which
    // is correct in the final review pass.
    given()
            .contentType("multipart/form-data")
    .when()
            .post("/characters/import-sheet")
    .then()
            .statusCode(anyOf(equalTo(400), equalTo(415)));
}
```

With static imports:
```java
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
```

Append to spec FIXME tracker.

- [ ] **Step 3:** Run, expect PASS.
- [ ] **Step 4:** Defer commit.

### Task 4.3: Rewrite remaining 4 character-service tests

For each of:
- `CharacterCreateIntegrationTest`
- `CharacterFindAllIntegrationTest`
- `CharacterUpdateIntegrationTest`
- `CharacterSheetDownloadIntegrationTest`

Per file:
- [ ] Read original (preserve existing `@Disabled` reasons verbatim)
- [ ] Identify category (B + D, with `@TestSecurity`)
- [ ] Apply matching template
- [ ] Run `mvn -pl services/character-service/character-service test -Dtest=<ClassName>` — expect PASS
- [ ] Defer commit to Task 4.4

### Task 4.4: Character-service verification + commit

- [ ] **Step 1:** `mvn -pl services/character-service/character-service test -Dtest='*IntegrationTest'` — PASS (preserved `@Disabled` count must match original).
- [ ] **Step 2:** Static checks.
- [ ] **Step 3:**
```bash
git add services/character-service/character-service/src/test/java/integration/
git commit -m "test(character-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase4-character-service-complete
```

---

## Phase 5 — `chat-service`

**Files in scope:** 7 files in `services/chat-service/chat-service/src/test/java/integration/chat/`

### Task 5.1: Audit

- [ ] **Step 1:** Read all 7 files. Conversation/message CRUD, mostly Category B/D (JWT-protected).

### Task 5.2 → 5.8: Per-file rewrites

For each of:
- `ConversationCreateIntegrationTest`
- `ConversationFindByIdIntegrationTest`
- `ConversationFindByUserIntegrationTest`
- `ConversationUpdateReadByIdIntegrationTest`
- `MessageSendIntegrationTest`
- `MessageFindByConversationIntegrationTest`
- `OnlineUsersFindIntegrationTest`

Per file:
- [ ] Read original
- [ ] Apply spec template (Category B + D for most)
- [ ] Run `mvn -pl services/chat-service/chat-service test -Dtest=<ClassName>` — expect PASS
- [ ] Defer commit

### Task 5.9: Chat-service verification + commit

- [ ] `mvn -pl services/chat-service/chat-service test -Dtest='*IntegrationTest'` — PASS, 7 classes.
- [ ] Static checks.
- [ ] Commit and tag `phase5-chat-service-complete`.

---

## Phase 6 — `auth-service` (Category E, WireMock)

**Files in scope:** 8 files in `services/auth-service/auth-service/src/test/java/integration/auth/` plus WireMock resources under `services/auth-service/auth-service/src/test/java/integration/resource/`.

### Task 6.1: Audit

- [ ] **Step 1:** Read all 8 files. Most are Category E (call user-service via WireMock for credential validation).

- [ ] **Step 2:** Confirm `UserServiceWireMockResource` (or similar) exists in `integration/resource/`. Note its URL property.

```bash
ls services/auth-service/auth-service/src/test/java/integration/resource/
```

- [ ] **Step 3:** Identify `anyOf(200, 201)` and `anyOf(400, 401)` workarounds in the original tests — auth-service has the most. Each becomes a FIXME entry.

### Task 6.2: Rewrite `CreateLoginTokensIntegrationTest`

- [ ] **Step 1:** Read original.
- [ ] **Step 2:** Rewrite as Category E. Use the WireMock injection pattern:

```java
@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@Transactional
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CreateLoginTokensIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectMockServer(UserServiceWireMockResource.class)
    private WireMockServer userServiceServer;

    @InjectRandom
    private CreateLoginTokensRequestViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldCreateLoginTokens() throws JsonProcessingException {
        // given
        var request = CreateLoginTokensRequestViewModelBuilder.from(payloadTemplate)
                .withUsername("gandalf")            // hardcoded: matches WireMock stub
                .withPassword("YouShallNotPass1")    // hardcoded: matches WireMock stub
                .build();

        userServiceServer.givenThat(
                get("/users/by-username/gandalf")
                        .willReturn(okJson("""
                                {"id":1,"username":"gandalf","passwordHash":"...","role":"PLAYER","active":true}
                                """)));

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/login-tokens")
        .then()
                .statusCode(201) // FIXME(integration-tests-rewrite): originally anyOf(200,201) — pick 201 (correct REST for resource creation)
                .extract().as(LoginTokensViewModel.class);

        // then
        assertThat(response.accessToken()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();
    }
}
```

> **Important:** if the chosen status (here 201) makes the test fail because the product still returns 200, mark the test `@Disabled` with reason `"awaiting product fix: returns 200 instead of 201 for resource creation"` and add an entry to the FIXME tracker. Do NOT loosen the assertion. We'll resolve all FIXMEs in the final pass.

- [ ] **Step 3:** Run:
```bash
mvn -pl services/auth-service/auth-service test -Dtest=CreateLoginTokensIntegrationTest
```
Expected: PASS or `@Disabled`-with-reason if status mismatch.

- [ ] **Step 4:** Defer commit.

### Task 6.3 → 6.9: Rewrite the remaining 7 auth-service tests

Same per-file procedure. The 7 remaining files (after `CreateLoginTokensIntegrationTest` done in Task 6.2):
- `RefreshLoginTokensIntegrationTest`
- `LogoutIntegrationTest`
- `LogoutAllIntegrationTest`
- `RequestPasswordResetIntegrationTest`
- `ResetPasswordIntegrationTest`
- `RequestOtpLoginIntegrationTest`
- `ValidateOtpLoginIntegrationTest`

For each:
- [ ] Read original, identify FIXME workarounds, append to tracker
- [ ] Rewrite with Category E template + WireMock stubs
- [ ] Run `mvn -pl services/auth-service/auth-service test -Dtest=<ClassName>` — PASS or `@Disabled`-with-reason
- [ ] Defer commit

### Task 6.10: Auth-service verification + commit

- [ ] **Step 1:** Full suite:
```bash
mvn -pl services/auth-service/auth-service test -Dtest='*IntegrationTest'
```
Expected: PASS. Disabled count = original disabled count + new disables explicitly added during rewrite (each new disable must have a tracker entry).

- [ ] **Step 2:** Static checks.

- [ ] **Step 3:** Commit and tag:
```bash
git add services/auth-service/auth-service/src/test/java/integration/
git commit -m "test(auth-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase6-auth-service-complete
```

---

## Phase 7 — `combat-service`

**Files in scope:** 13 files in `services/combat-service/combat-service/src/test/java/integration/combat/`

### Task 7.1: Audit

- [ ] **Step 1:** Read all 13 files. Encounter/participant CRUD with JWT — Categories B + D heavy.

- [ ] **Step 2:** Identify dependencies between tests (e.g. `ParticipantAdd` depends on an existing `Encounter` — likely seeded via `@PrepareEntities`).

### Task 7.2 → 7.14: Per-file rewrites

For each of:
- `EncounterCreateIntegrationTest`
- `EncounterUpdateIntegrationTest`
- `EncounterDeleteIntegrationTest`
- `EncounterCompleteIntegrationTest`
- `EncounterFindByIdIntegrationTest`
- `EncounterFindByCampaignIntegrationTest`
- `ParticipantAddIntegrationTest`
- `ParticipantUpdateIntegrationTest`
- `ParticipantDeleteIntegrationTest`
- `InitiativeStartIntegrationTest`
- `TurnAdvanceIntegrationTest`
- `TurnOrderFindIntegrationTest`
- `DifficultyCalculateIntegrationTest`

Per file:
- [ ] Read, identify category and dependencies
- [ ] Rewrite using matching template
- [ ] Run `mvn -pl services/combat-service/combat-service test -Dtest=<ClassName>` — PASS
- [ ] Defer commit

### Task 7.15: Combat-service verification + commit

- [ ] **Step 1:** `mvn -pl services/combat-service/combat-service test -Dtest='*IntegrationTest'` — PASS, 13 classes.
- [ ] **Step 2:** Static checks.
- [ ] **Step 3:** Commit and tag:
```bash
git add services/combat-service/combat-service/src/test/java/integration/
git commit -m "test(combat-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase7-combat-service-complete
```

---

## Phase 8 — `document-qa-service` (Category E heavy)

**Files in scope:** 15 files in `services/document-qa-service/document-qa-service/src/test/java/integration/docqa/`

### Task 8.1: Audit

- [ ] **Step 1:** Read all 15 files. Many call LLM via WireMock — Category E.
- [ ] **Step 2:** Identify the `LlmServiceWireMockResource` (or equivalent) and confirm it's set up under `integration/resource/`.
- [ ] **Step 3:** Note `@Disabled` tests (e.g. `AskIntegrationTest.shouldReturn400ForInvalidAskRequest` is currently disabled with a `KNOWN BUG` reason — preserve verbatim).

### Task 8.2 → 8.16: Per-file rewrites

For each of:
- `AskIntegrationTest`
- `CreateSystemLlmConfigurationIntegrationTest`
- `CreateUserLlmConfigurationIntegrationTest`
- `GetSystemLlmConfigurationsIntegrationTest`
- `GetUserLlmConfigurationsIntegrationTest`
- `DeleteSystemLlmConfigurationIntegrationTest`
- `DeleteUserLlmConfigurationIntegrationTest`
- `ActivateSystemLlmConfigurationIntegrationTest`
- `ActivateUserLlmConfigurationIntegrationTest`
- `TriggerIngestionIntegrationTest`
- `GetIngestionStatusIntegrationTest`
- `GetConversationByIdIntegrationTest`
- `GetConversationMessagesIntegrationTest`
- `GetConversationsIntegrationTest`
- `DeleteConversationIntegrationTest`

Per file:
- [ ] Read, identify category, preserve `@Disabled` comments verbatim
- [ ] Rewrite using matching template (most are E with LLM mock)
- [ ] Run `mvn -pl services/document-qa-service/document-qa-service test -Dtest=<ClassName>` — PASS or preserved-`@Disabled`
- [ ] Defer commit

### Task 8.17: Document-qa-service verification + commit

- [ ] **Step 1:** `mvn -pl services/document-qa-service/document-qa-service test -Dtest='*IntegrationTest'` — PASS, 15 classes.
- [ ] **Step 2:** Static checks.
- [ ] **Step 3:** Commit and tag:
```bash
git add services/document-qa-service/document-qa-service/src/test/java/integration/
git commit -m "test(document-qa-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase8-document-qa-service-complete
```

---

## Phase 9 — `campaign-service`

**Files in scope:** 18 files in `services/campaign-service/campaign-service/src/test/java/integration/campaign/`

### Task 9.1: Audit

- [ ] **Step 1:** Read all 18 files. Campaign/note/quest/member CRUD — Categories B + C + D.
- [ ] **Step 2:** Note shared seed data: `Campaign` is the parent of `CampaignNote`, `CampaignQuest`, `CampaignMember` — many tests need a campaign seeded first via `@PrepareEntities(CampaignEntityProvider.class)`.

### Task 9.2 → 9.19: Per-file rewrites

For each of the 18 files:
- `CampaignCreateIntegrationTest`
- `CampaignUpdateIntegrationTest`
- `CampaignDeleteIntegrationTest`
- `CampaignFindByIdIntegrationTest`
- `CampaignFindAllIntegrationTest`
- `CampaignMemberAddIntegrationTest`
- `CampaignMemberRemoveIntegrationTest`
- `CampaignMemberListIntegrationTest`
- `CampaignNoteCreateIntegrationTest`
- `CampaignNoteListIntegrationTest`
- `CampaignNoteFindByIdIntegrationTest`
- `CampaignNoteUpdateIntegrationTest`
- `CampaignNoteDeleteIntegrationTest`
- `CampaignQuestCreateIntegrationTest`
- `CampaignQuestListIntegrationTest`
- `CampaignQuestFindByIdIntegrationTest`
- `CampaignQuestUpdateIntegrationTest`
- `CampaignQuestDeleteIntegrationTest`

Per file:
- [ ] Read, classify
- [ ] Apply spec templates B/C/D, paying attention to parent-child seeding
- [ ] Run `mvn -pl services/campaign-service/campaign-service test -Dtest=<ClassName>` — PASS
- [ ] Defer commit

### Task 9.20: Campaign-service verification + commit

- [ ] **Step 1:** Full suite — `mvn -pl services/campaign-service/campaign-service test -Dtest='*IntegrationTest'` — PASS, 18 classes.
- [ ] **Step 2:** Static checks.
- [ ] **Step 3:** Commit and tag:
```bash
git add services/campaign-service/campaign-service/src/test/java/integration/
git commit -m "test(campaign-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase9-campaign-service-complete
```

---

## Phase 10 — `compendium-service`

**Files in scope:** 36 files in `services/compendium-service/compendium-service/src/test/java/integration/compendium/`

### Task 10.1: Audit

- [ ] **Step 1:** Read all 36 files. They all follow the same pattern: read-only on Flyway-seeded data. Pairs of `*FindAll` + `*FindById` for each entity (Alignment, ArmorType, CharacterClass, etc.).

- [ ] **Step 2:** Confirm Flyway migrations seed the data (no `@PrepareEntities` needed; tests rely on the migrations).

```bash
ls services/compendium-service/compendium-service-adapter-outbound/src/main/resources/db/migration/ 2>/dev/null
```

### Task 10.2: Rewrite the first pair as a template — `AlignmentFindAllIntegrationTest` + `AlignmentFindByIdIntegrationTest`

- [ ] **Step 1:** Read both originals.
- [ ] **Step 2:** Rewrite `AlignmentFindAllIntegrationTest`:

```java
package integration.compendium;

import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AlignmentFindAllIntegrationTest {

    @Test
    void shouldFindAllAlignments() {
        // when
        var response = given()
        .when()
                .get("/alignments")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(new TypeRef<List<AlignmentViewModel>>() {});

        // then
        assertThat(response).isNotEmpty();
        assertThat(response).allSatisfy(alignment -> {
            assertThat(alignment.id()).isNotNull();
            assertThat(alignment.name()).isNotBlank();
        });
    }
}
```

- [ ] **Step 3:** Rewrite `AlignmentFindByIdIntegrationTest`:

```java
package integration.compendium;

import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AlignmentFindByIdIntegrationTest {

    @Test
    void shouldFindAlignmentById() {
        // when
        var response = given()
        .when()
                .get("/alignments/{id}", 1L) // hardcoded: first seeded alignment from Flyway migration
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(AlignmentViewModel.class);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isNotBlank();
    }

    @Test
    void shouldFailWhenAlignmentDoesNotExist() {
        // when / then
        given()
        .when()
                .get("/alignments/{id}", 999_999L) // hardcoded: id outside any seed
        .then()
                .statusCode(404);
    }
}
```

- [ ] **Step 4:** Run:
```bash
mvn -pl services/compendium-service/compendium-service test -Dtest='Alignment*IntegrationTest'
```
Expected: PASS, 2 classes green, 3 test methods.

- [ ] **Step 5:** Defer commit.

### Task 10.3 → 10.19: Rewrite the remaining 17 pairs

Apply the same Find-All/Find-By-Id template to each entity pair (17 pairs remaining after Alignment from Task 10.2):
- ArmorType
- Background
- CharacterClass
- Condition
- DamageType
- Equipment
- Feat
- Language
- MagicItem
- Monster
- ProficiencyType
- Skill
- Species
- Spell
- SpellSchool
- ToolType
- WeaponType

Per pair:
- [ ] Read both originals
- [ ] Replace with the Alignment template, substituting the entity/route name
- [ ] Run `mvn -pl services/compendium-service/compendium-service test -Dtest='<EntityName>*IntegrationTest'` — PASS
- [ ] Defer commit

> **Note:** because compendium tests are extremely homogeneous, this is the easiest large phase. Once you've verified two pairs by hand, the rest is mechanical substitution. Do NOT skimp on per-pair verification — run each pair after rewriting.

### Task 10.20: Compendium-service verification + commit

- [ ] **Step 1:** Full suite:
```bash
mvn -pl services/compendium-service/compendium-service test -Dtest='*IntegrationTest'
```
Expected: PASS, 36 classes green.

- [ ] **Step 2:** Static checks (the usual three greps).

- [ ] **Step 3:** Commit and tag:
```bash
git add services/compendium-service/compendium-service/src/test/java/integration/
git commit -m "test(compendium-service): rewrite all integration tests

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
git tag phase10-compendium-service-complete
```

---

## Phase 11 — Global verification

### Task 11.1: Full project build

- [ ] **Step 1:** Run the full project test suite from the root:

```bash
mvn clean test
```

Expected: BUILD SUCCESS. All services green. Disabled count = (original disabled) + (FIXME-tracker entries explicitly disabled during rewrite).

If anything fails at this point (a test that worked in isolation but fails in the full build), diagnose: typically class-level `@PrepareEntities` data leaks across tests. Fix by adding the missing `@DeleteEntities` cleanup, retest, commit as `fix(integration-tests): cleanup leak in <Service>`.

### Task 11.2: Coverage report comparison

- [ ] **Step 1:** Generate the coverage report:

```bash
mvn jacoco:report
```

- [ ] **Step 2:** For each service, compare the per-package line coverage with the value before the rewrite (use `git show pre-phase-common-test-extended:<jacoco-report-path>` if a baseline report was committed, otherwise note this step is informational only — JaCoCo isn't checked into git). Coverage should not drop more than 1 percentage point per service.

### Task 11.3: FIXME tracker walkthrough with the user

- [ ] **Step 1:** Open `docs/superpowers/specs/2026-04-09-integration-tests-rewrite-design.md` and read the FIXME Tracker table.

- [ ] **Step 2:** For each entry, present the case to the user in the format:

```
FIXME #N — services/<svc>/...<File>.java:<line>
Original assertion: <e.g. anyOf(200,201)>
Suspected cause: <e.g. POST returns 200 but should return 201 (REST resource creation)>
Proposed action: [1] fix product to return 201 / [2] fix test to expect 200 / [3] keep loose with reason
```

- [ ] **Step 3:** Wait for user decision per entry. Apply the chosen fix as a separate commit per FIXME (`fix(<svc>): <description>` for product fixes, `test(<svc>): <description>` for test fixes).

- [ ] **Step 4:** After all FIXME entries are resolved, re-run `mvn clean test` from the root — must be green with zero `@Disabled` (other than originals preserved verbatim).

### Task 11.4: Code review request

- [ ] **Step 1:** Invoke the `superpowers:requesting-code-review` skill on the entire branch from `pre-phase-common-test-extended` to HEAD. Report findings to the user. Apply any blocking feedback as additional commits.

### Task 11.5: Update spec FIXME tracker with resolutions

- [ ] **Step 1:** Edit the spec file's FIXME Tracker table, marking each row as `RESOLVED — <commit-sha>`.

- [ ] **Step 2:** Commit:
```bash
git add docs/superpowers/specs/2026-04-09-integration-tests-rewrite-design.md
git commit -m "docs: mark integration-tests-rewrite FIXMEs as resolved

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

### Task 11.6: Final tag

- [ ] **Step 1:**
```bash
git tag integration-tests-rewrite-complete
git log --oneline pre-phase-common-test-extended..HEAD
```

Expected: ~13 service commits + ~N FIXME-resolution commits + 1 docs commit + 1 code-review commit (if any). All tagged and visible in the log.

---

## Spec Coverage Map

| Spec section | Covered by |
|---|---|
| §1 common-test extension | Phase 0 (Tasks 0.1, 0.2, 0.3) |
| §2 code standards | Every per-file rewrite step (Phases 1–10) |
| §3 test categories A/B/C/D/E | Templates referenced in every per-file step; canonical examples in Task 1.2 (B+C), Task 1.3 (B-read), Task 6.2 (E with WireMock), Task 4.2 (multipart), Task 10.2 (read-only) |
| §4 service plan | Phases 1–10, one per service |
| §5 verification strategy | Per-service: Task X.last in each phase. Global: Phase 11 (Tasks 11.1–11.6) |
| FIXME tracker | Populated during rewrites; walked through in Task 11.3 |

---

## Notes for the Executor

- **Read the spec before each phase.** The templates in Section 3 are the source of truth. This plan is the execution sequence; the spec is the style.
- **Per-service gate is rigid.** Do not start the next phase if the current `mvn test -pl <svc>` is red. Diagnose, fix, retest, then move on.
- **Per-file commits during Phase 1 only.** From Phase 2 onward, one commit per service. Phase 11 fixes get individual commits.
- **`@Disabled` tests are preserved verbatim** — including their reason string. Do not unblock or rewrite the disabled body.
- **FIXME entries go into the spec doc, not into the plan.** The plan is read-only after writing; the spec's FIXME tracker is the live document.
- **If a rewrite reveals a bug in `common-test`** (e.g. `@NamedParam` doesn't bind correctly for `Long` vs `long`), STOP, return to Phase 0, fix the extension, add a unit test, retest the affected service, then continue.
