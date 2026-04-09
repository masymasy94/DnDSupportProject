# Integration Tests Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
>
> **Integration test skill:** Use the `integration-tests` skill from `~/.claude/skills/integration-tests/SKILL.md` as reference for patterns and conventions.

**Goal:** Add comprehensive integration tests for all DnD Platform services, testing REST endpoints end-to-end with real database, messaging, and security.

**Architecture:** Each service's assembly module (`{service}/{service}/`) gets `src/test/resources/application.properties` (disables Vault, uses Quarkus DevServices for PostgreSQL/RabbitMQ/Redis) and integration tests under `src/test/java/integration/`. Tests use `@QuarkusTest` + REST-Assured + `@TestSecurity` for JWT-protected endpoints. Shared test utilities (TestEntityProvider, @PrepareEntities, @DeleteEntities) live in `common-test`.

**Tech Stack:** Quarkus 3.31.4, JUnit 5, REST-Assured, AssertJ, Quarkus TestSecurity, Quarkus DevServices (Testcontainers), Flyway

**Prerequisites:** Docker running (DevServices starts PostgreSQL, RabbitMQ, Redis containers automatically).

---

## Phase 1: Test Infrastructure

### Task 1: Create TestEntityProvider interface and JUnit extensions in common-test

**Files:**
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/TestEntityProvider.java`
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/PrepareEntities.java`
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntities.java`
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntitiesList.java`
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/PrepareEntitiesExtension.java`
- Create: `services/common-test/src/main/java/com/dndplatform/test/entity/DeleteEntitiesExtension.java`
- Modify: `services/common-test/pom.xml` (add Jakarta Persistence + CDI dependencies)

- [ ] **Step 1: Add dependencies to common-test pom.xml**

Read `services/common-test/pom.xml`. Add these dependencies (provided scope — assembly modules supply the runtime):

```xml
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>jakarta.enterprise</groupId>
    <artifactId>jakarta.enterprise.cdi-api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>jakarta.transaction</groupId>
    <artifactId>jakarta.transaction-api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>provided</scope>
</dependency>
```

- [ ] **Step 2: Create TestEntityProvider interface**

```java
package com.dndplatform.test.entity;

import java.util.List;

public interface TestEntityProvider {
    List<Object> provideEntities();
}
```

- [ ] **Step 3: Create @PrepareEntities annotation**

```java
package com.dndplatform.test.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrepareEntities {
    Class<? extends TestEntityProvider> value();
}
```

- [ ] **Step 4: Create @DeleteEntities annotation (repeatable)**

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
}
```

```java
package com.dndplatform.test.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteEntitiesList {
    DeleteEntities[] value();
}
```

- [ ] **Step 5: Create PrepareEntitiesExtension**

```java
package com.dndplatform.test.entity;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PrepareEntitiesExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            var annotation = method.getAnnotation(PrepareEntities.class);
            if (annotation != null) {
                var provider = instantiate(annotation.value());
                var em = CDI.current().select(EntityManager.class).get();
                var tx = CDI.current().select(UserTransaction.class).get();
                try {
                    tx.begin();
                    provider.provideEntities().forEach(em::persist);
                    em.flush();
                    tx.commit();
                } catch (Exception e) {
                    try { tx.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException("Failed to prepare entities", e);
                }
            }
        });
    }

    private TestEntityProvider instantiate(Class<? extends TestEntityProvider> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + clazz.getName(), e);
        }
    }
}
```

- [ ] **Step 6: Create DeleteEntitiesExtension**

```java
package com.dndplatform.test.entity;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteEntitiesExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            var annotations = method.getAnnotationsByType(DeleteEntities.class);
            if (annotations.length > 0) {
                var em = CDI.current().select(EntityManager.class).get();
                var tx = CDI.current().select(UserTransaction.class).get();
                try {
                    tx.begin();
                    for (var annotation : annotations) {
                        var entityName = annotation.from().getSimpleName();
                        var deleted = em.createQuery("DELETE FROM " + entityName).executeUpdate();
                        if (annotation.expectedRowCount() >= 0) {
                            assertThat(deleted).isEqualTo(annotation.expectedRowCount());
                        }
                    }
                    tx.commit();
                } catch (Exception e) {
                    try { tx.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException("Failed to delete entities", e);
                }
            }
        });
    }
}
```

- [ ] **Step 7: Verify common-test compiles**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
compile -pl services/common-test --no-transfer-progress
```
Expected: BUILD SUCCESS

- [ ] **Step 8: Commit**

```bash
git add services/common-test/
git commit -m "feat: add TestEntityProvider and JUnit extensions for integration tests"
```

---

### Task 2: Add quarkus-test-security dependency to all assembly modules

**Files:**
- Modify: `services/user-service/pom.xml` (parent pom — dependencyManagement)
- Modify: `services/user-service/user-service/pom.xml`
- Modify: `services/campaign-service/campaign-service/pom.xml`
- Modify: `services/auth-service/auth-service/pom.xml`
- Modify: `services/chat-service/chat-service/pom.xml`
- Modify: `services/asset-service/asset-service/pom.xml`
- Modify: `services/character-service/character-service/pom.xml`
- Modify: `services/notification-service/notification-service/pom.xml`
- Modify: `services/compendium-service/compendium-service/pom.xml`

- [ ] **Step 1: Add quarkus-test-security and common-test to each assembly module's pom.xml**

For EACH assembly module pom.xml listed above, add inside `<dependencies>`:

```xml
<!-- Integration test support -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-security</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.dndplatform</groupId>
    <artifactId>common-test</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

- [ ] **Step 2: Verify one service compiles**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
compile -pl services/user-service/user-service -am --no-transfer-progress
```
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add services/*/pom.xml services/*/*/pom.xml
git commit -m "feat: add quarkus-test-security and common-test dependencies for integration tests"
```

---

## Phase 2: User Service Integration Tests

### Task 3: Create user-service test configuration

**Files:**
- Create: `services/user-service/user-service/src/test/resources/application.properties`

- [ ] **Step 1: Create test application.properties**

This file overrides the main `application.properties` during `@QuarkusTest`. Disables Vault, lets DevServices handle infrastructure.

```properties
# ===========================
# Integration Test Configuration
# ===========================
# Disables Vault — all secrets provided directly below.
# Quarkus DevServices auto-starts PostgreSQL, RabbitMQ, Redis via Testcontainers.

# --- Vault: disabled ---
quarkus.vault.url=

# --- Database: DevServices auto-starts PostgreSQL ---
quarkus.datasource.db-kind=postgresql
# Override Vault-sourced expressions so they resolve (DevServices overrides at runtime)
db.username=quarkus
db.password=quarkus
user-db.url=

# --- Flyway: run migrations on fresh test DB ---
quarkus.flyway.migrate-at-start=true
quarkus.flyway.baseline-on-migrate=true
quarkus.flyway.baseline-version=0
quarkus.flyway.locations=classpath:db/migration

# --- RabbitMQ: DevServices auto-starts container ---
# Override Vault-sourced expressions
rabbitmq-host=localhost
rabbitmq-port=5672
rabbitmq-username=guest
rabbitmq-password=guest
rabbitmq-virtual-host=/

# --- Redis: DevServices auto-starts container ---
quarkus.redis.hosts=redis://localhost:6379

# --- JWT: disabled (tests use @TestSecurity) ---
quarkus.smallrye-jwt.enabled=false
mp.jwt.verify.publickey=NONE
mp.jwt.verify.issuer=test-issuer
jwt.public-key=NONE
jwt-config.issuer=test-issuer

# --- Telemetry: off ---
quarkus.otel.enabled=false
quarkus.otel.sdk.disabled=true

# --- Logging: human-readable ---
quarkus.log.console.json=false
quarkus.log.level=INFO
```

- [ ] **Step 2: Verify SmokeTest still passes**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/user-service/user-service -Dtest=SmokeTest --no-transfer-progress
```
Expected: Tests run: 1, Failures: 0

If Vault extension fails to start with empty URL, try adding:
```properties
quarkus.vault.authentication.client-token=disabled
```

- [ ] **Step 3: Commit**

```bash
git add services/user-service/user-service/src/test/resources/
git commit -m "feat: add user-service test configuration for integration tests"
```

---

### Task 4: User-service UserEntityProvider

**Files:**
- Create: `services/user-service/user-service/src/test/java/integration/user/UserEntityProvider.java`

- [ ] **Step 1: Create UserEntityProvider**

```java
package integration.user;

import com.dndplatform.test.entity.TestEntityProvider;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import io.quarkus.elytron.security.common.BcryptUtil;

import java.time.LocalDateTime;
import java.util.List;

public class UserEntityProvider implements TestEntityProvider {

    public static final String USERNAME = "testuser";
    public static final String EMAIL = "testuser@example.com";
    public static final String PASSWORD = "TestPassword123";
    public static final String PASSWORD_HASH = BcryptUtil.bcryptHash(PASSWORD);
    public static final String ROLE = "PLAYER";

    @Override
    public List<Object> provideEntities() {
        return List.of(getEntity());
    }

    public static UserEntity getEntity() {
        var entity = new UserEntity();
        entity.username = USERNAME;
        entity.email = EMAIL;
        entity.passwordHash = PASSWORD_HASH;
        entity.role = ROLE;
        entity.active = true;
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
}
```

Note: If `UserEntity` uses private fields with getters/setters instead of public fields, adjust accordingly. Check the entity class.

- [ ] **Step 2: Verify it compiles**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test-compile -pl services/user-service/user-service --no-transfer-progress
```
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add services/user-service/user-service/src/test/java/integration/
git commit -m "feat: add UserEntityProvider for integration tests"
```

---

### Task 5: User-service register endpoint integration tests

**Files:**
- Create: `services/user-service/user-service/src/test/java/integration/user/UserRegisterIntegrationTest.java`

- [ ] **Step 1: Write UserRegisterIntegrationTest**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserRegisterIntegrationTest {

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldRegisterNewUser() {
        var request = new UserRegisterViewModel("gandalf", "gandalf@shire.com", "MyPassword1");

        var response = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.username()).isEqualTo("gandalf");
        assertThat(response.email()).isEqualTo("gandalf@shire.com");
        assertThat(response.role()).isEqualTo("PLAYER");
        assertThat(response.active()).isTrue();
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void shouldReturn400WhenUsernameIsBlank() {
        var request = new UserRegisterViewModel("", "valid@email.com", "password");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() {
        var request = new UserRegisterViewModel("validuser", "not-an-email", "password");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenPasswordIsBlank() {
        var request = new UserRegisterViewModel("validuser", "valid@email.com", "");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenUsernameHasInvalidChars() {
        var request = new UserRegisterViewModel("invalid user!", "valid@email.com", "password");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturn409WhenUsernameAlreadyExists() {
        var request = new UserRegisterViewModel(
                UserEntityProvider.USERNAME, "different@email.com", "password");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(409);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturn409WhenEmailAlreadyExists() {
        var request = new UserRegisterViewModel(
                "differentuser", UserEntityProvider.EMAIL, "password");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(409);
    }
}
```

- [ ] **Step 2: Run tests**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/user-service/user-service -Dtest=UserRegisterIntegrationTest --no-transfer-progress
```
Expected: Tests run: 7, Failures: 0

Fix any failures before proceeding.

- [ ] **Step 3: Commit**

```bash
git add services/user-service/user-service/src/test/java/integration/
git commit -m "feat: add UserRegister integration tests"
```

---

### Task 6: User-service find and search endpoint integration tests

**Files:**
- Create: `services/user-service/user-service/src/test/java/integration/user/UserFindByIdIntegrationTest.java`
- Create: `services/user-service/user-service/src/test/java/integration/user/UserFindAllIntegrationTest.java`
- Create: `services/user-service/user-service/src/test/java/integration/user/UserSearchIntegrationTest.java`
- Create: `services/user-service/user-service/src/test/java/integration/user/UserFindByEmailIntegrationTest.java`

- [ ] **Step 1: Write UserFindByIdIntegrationTest**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindByIdIntegrationTest {

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserById() {
        // First register a user to get a known ID
        var registered = given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"findme","email":"findme@test.com","password":"pass123"}
                """)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().as(UserViewModel.class);

        var response = given()
        .when()
            .get("/users/" + registered.id())
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.id()).isEqualTo(registered.id());
        assertThat(response.username()).isEqualTo("findme");
        assertThat(response.email()).isEqualTo("findme@test.com");
    }

    @Test
    void shouldReturn404WhenUserNotFound() {
        given()
        .when()
            .get("/users/999999")
        .then()
            .statusCode(404);
    }
}
```

- [ ] **Step 2: Write UserFindAllIntegrationTest (requires @TestSecurity)**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindAllIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturnPagedUsers() {
        var response = given()
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(PagedUserViewModel.class);

        assertThat(response.content()).isNotEmpty();
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users")
        .then()
            .statusCode(401);
    }
}
```

- [ ] **Step 3: Write UserSearchIntegrationTest**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserSearchIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldSearchByUsername() {
        var response = given()
            .queryParam("query", UserEntityProvider.USERNAME)
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(PagedUserViewModel.class);

        assertThat(response.content()).isNotEmpty();
        assertThat(response.content().getFirst().username()).isEqualTo(UserEntityProvider.USERNAME);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyForNoMatch() {
        var response = given()
            .queryParam("query", "nonexistentuser12345")
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(PagedUserViewModel.class);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("query", "test")
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users/search")
        .then()
            .statusCode(401);
    }
}
```

- [ ] **Step 4: Write UserFindByEmailIntegrationTest**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindByEmailIntegrationTest {

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserByEmail() {
        var request = new UserFindByEmailViewModel(UserEntityProvider.EMAIL);

        var response = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users/email-lookup")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
    }

    @Test
    void shouldReturn404WhenEmailNotFound() {
        var request = new UserFindByEmailViewModel("nonexistent@email.com");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users/email-lookup")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() {
        var request = new UserFindByEmailViewModel("not-an-email");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users/email-lookup")
        .then()
            .statusCode(400);
    }
}
```

- [ ] **Step 5: Run all find/search tests**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/user-service/user-service \
-Dtest="UserFindByIdIntegrationTest,UserFindAllIntegrationTest,UserSearchIntegrationTest,UserFindByEmailIntegrationTest" \
--no-transfer-progress
```
Expected: All tests pass

- [ ] **Step 6: Commit**

```bash
git add services/user-service/user-service/src/test/java/integration/
git commit -m "feat: add user-service find and search integration tests"
```

---

### Task 7: User-service internal endpoint integration tests

**Files:**
- Create: `services/user-service/user-service/src/test/java/integration/user/UserCredentialsValidateIntegrationTest.java`
- Create: `services/user-service/user-service/src/test/java/integration/user/UserUpdatePasswordIntegrationTest.java`

- [ ] **Step 1: Write UserCredentialsValidateIntegrationTest**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserCredentialsValidateIntegrationTest {

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldValidateCorrectCredentials() {
        var request = new UserCredentialsValidateViewModel(
                UserEntityProvider.USERNAME, UserEntityProvider.PASSWORD);

        var response = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturn401ForWrongPassword() {
        var request = new UserCredentialsValidateViewModel(
                UserEntityProvider.USERNAME, "WrongPassword");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(401);
    }

    @Test
    void shouldReturn401ForNonexistentUser() {
        var request = new UserCredentialsValidateViewModel(
                "nonexistentuser", "password");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(401);
    }
}
```

- [ ] **Step 2: Write UserUpdatePasswordIntegrationTest**

```java
package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserUpdatePasswordIntegrationTest {

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldUpdatePasswordSuccessfully() {
        // Register user first
        var registered = given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"pwduser","email":"pwduser@test.com","password":"OldPassword1"}
                """)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().as(UserViewModel.class);

        // Update password
        var updateRequest = new UserUpdatePasswordViewModel("NewPassword1");

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .put("/users/" + registered.id() + "/password")
        .then()
            .statusCode(204);

        // Verify new password works
        var validateRequest = new UserCredentialsValidateViewModel("pwduser", "NewPassword1");

        given()
            .contentType(ContentType.JSON)
            .body(validateRequest)
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(200);
    }

    @Test
    void shouldReturn404WhenUserNotFound() {
        var request = new UserUpdatePasswordViewModel("NewPassword1");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .put("/users/999999/password")
        .then()
            .statusCode(404);
    }
}
```

- [ ] **Step 3: Run all user-service integration tests**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/user-service/user-service \
-Dtest="integration.**" --no-transfer-progress
```
Expected: All tests pass (SmokeTest + all integration tests)

- [ ] **Step 4: Commit**

```bash
git add services/user-service/user-service/src/test/java/integration/
git commit -m "feat: add user-service credentials and password integration tests"
```

---

## Phase 3: Campaign Service Integration Tests

### Task 8: Create campaign-service test configuration

**Files:**
- Create: `services/campaign-service/campaign-service/src/test/resources/application.properties`

- [ ] **Step 1: Create test application.properties**

```properties
# ===========================
# Integration Test Configuration
# ===========================

# --- Vault: disabled ---
quarkus.vault.url=

# --- Database: DevServices auto-starts PostgreSQL ---
quarkus.datasource.db-kind=postgresql
db.username=quarkus
db.password=quarkus
campaign-db.url=

# --- Flyway ---
quarkus.flyway.migrate-at-start=true
quarkus.hibernate-orm.database.generation=none

# --- RabbitMQ: DevServices auto-starts container ---
rabbitmq-host=localhost
rabbitmq-port=5672
rabbitmq-username=guest
rabbitmq-password=guest
rabbitmq-virtual-host=/

# --- JWT: disabled (tests use @TestSecurity) ---
quarkus.smallrye-jwt.enabled=false
mp.jwt.verify.publickey=NONE
mp.jwt.verify.issuer=test-issuer
jwt.public-key=NONE
jwt-config.issuer=test-issuer

# --- REST Clients: override Vault-sourced URLs ---
# These services are not called in basic CRUD tests.
# If tests hit cross-service endpoints, use WireMock.
campaign-db.auth-service-url=http://localhost:19999
campaign-db.character-service-url=http://localhost:19998
campaign-db.asset-service-url=http://localhost:19997

# --- Telemetry: off ---
quarkus.otel.enabled=false
quarkus.otel.sdk.disabled=true

# --- Logging ---
quarkus.log.console.json=false
quarkus.log.level=INFO
```

- [ ] **Step 2: Verify SmokeTest passes**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/campaign-service/campaign-service -Dtest=SmokeTest --no-transfer-progress
```
Expected: BUILD SUCCESS, 1 test passed

- [ ] **Step 3: Commit**

```bash
git add services/campaign-service/campaign-service/src/test/resources/
git commit -m "feat: add campaign-service test configuration for integration tests"
```

---

### Task 9: Campaign-service entity providers

**Files:**
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignEntityProvider.java`
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignWithMemberEntityProvider.java`

- [ ] **Step 1: Create CampaignEntityProvider**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.test.entity.TestEntityProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CampaignEntityProvider implements TestEntityProvider {

    public static final String NAME = "Test Campaign";
    public static final String DESCRIPTION = "A test campaign for integration tests";
    public static final Long DUNGEON_MASTER_ID = 1L;
    public static final String STATUS = "DRAFT";
    public static final Integer MAX_PLAYERS = 6;

    @Override
    public List<Object> provideEntities() {
        return List.of(getEntity());
    }

    public static CampaignEntity getEntity() {
        var entity = new CampaignEntity();
        entity.name = NAME;
        entity.description = DESCRIPTION;
        entity.dungeonMasterId = DUNGEON_MASTER_ID;
        entity.status = STATUS;
        entity.maxPlayers = MAX_PLAYERS;
        entity.createdAt = LocalDateTime.now();
        entity.members = new ArrayList<>();
        entity.notes = new ArrayList<>();
        entity.quests = new ArrayList<>();
        return entity;
    }
}
```

- [ ] **Step 2: Create CampaignWithMemberEntityProvider**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.test.entity.TestEntityProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CampaignWithMemberEntityProvider implements TestEntityProvider {

    public static final Long MEMBER_USER_ID = 2L;
    public static final String MEMBER_ROLE = "PLAYER";

    @Override
    public List<Object> provideEntities() {
        var campaign = CampaignEntityProvider.getEntity();

        var member = new CampaignMemberEntity();
        member.campaign = campaign;
        member.userId = MEMBER_USER_ID;
        member.role = MEMBER_ROLE;
        member.joinedAt = LocalDateTime.now();

        campaign.members = new ArrayList<>(List.of(member));

        return List.of(campaign);
    }
}
```

Note: If entity fields are private, adjust to use setters. Check the entity classes.

- [ ] **Step 3: Verify compiles**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test-compile -pl services/campaign-service/campaign-service --no-transfer-progress
```
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add services/campaign-service/campaign-service/src/test/java/integration/
git commit -m "feat: add campaign-service entity providers for integration tests"
```

---

### Task 10: Campaign-service CRUD integration tests

**Files:**
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignCreateIntegrationTest.java`
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignFindIntegrationTest.java`
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignUpdateIntegrationTest.java`
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignDeleteIntegrationTest.java`

- [ ] **Step 1: Write CampaignCreateIntegrationTest**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignCreateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignEntity.class)
    void shouldCreateCampaign() {
        var request = new CreateCampaignRequest(1L, "Curse of Strahd",
                "Gothic horror in Barovia", 6, null);

        var response = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract().as(CampaignViewModel.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Curse of Strahd");
        assertThat(response.description()).isEqualTo("Gothic horror in Barovia");
        assertThat(response.dungeonMasterId()).isEqualTo(1L);
        assertThat(response.maxPlayers()).isEqualTo(6);
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenNameIsBlank() {
        var request = new CreateCampaignRequest(1L, "", "description", 6, null);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(400);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenMaxPlayersExceeds20() {
        var request = new CreateCampaignRequest(1L, "Campaign", "desc", 25, null);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        var request = new CreateCampaignRequest(1L, "Campaign", "desc", 6, null);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(401);
    }
}
```

- [ ] **Step 2: Write CampaignFindIntegrationTest**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignFindIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(CampaignEntityProvider.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldFindAllCampaigns() {
        var response = given()
            .queryParam("userId", CampaignEntityProvider.DUNGEON_MASTER_ID)
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/campaigns")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("content", CampaignViewModel.class);

        assertThat(response).isNotEmpty();
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(CampaignEntityProvider.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldFindCampaignById() {
        // First create via API to get known ID
        var created = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Find Me","description":"Test","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        var response = given()
        .when()
            .get("/campaigns/" + created.id())
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(CampaignViewModel.class);

        assertThat(response.id()).isEqualTo(created.id());
        assertThat(response.name()).isEqualTo("Find Me");
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenCampaignNotFound() {
        given()
        .when()
            .get("/campaigns/999999")
        .then()
            .statusCode(404);
    }
}
```

- [ ] **Step 3: Write CampaignUpdateIntegrationTest**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateCampaign() {
        // Create campaign first
        var created = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Original","description":"Original desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        // Update it
        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Updated Name","description":"Updated desc","status":"ACTIVE","maxPlayers":6}
                """)
        .when()
            .put("/campaigns/" + created.id())
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(CampaignViewModel.class);

        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.description()).isEqualTo("Updated desc");
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.maxPlayers()).isEqualTo(6);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenUpdatingNonexistentCampaign() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Name","description":"desc","status":"ACTIVE","maxPlayers":4}
                """)
        .when()
            .put("/campaigns/999999")
        .then()
            .statusCode(404);
    }
}
```

- [ ] **Step 4: Write CampaignDeleteIntegrationTest**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignDeleteIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignEntity.class)
    void shouldDeleteCampaign() {
        // Create first
        var created = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Delete Me","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        // Delete
        given()
            .queryParam("userId", 1)
        .when()
            .delete("/campaigns/" + created.id())
        .then()
            .statusCode(204);

        // Verify gone
        given()
        .when()
            .get("/campaigns/" + created.id())
        .then()
            .statusCode(404);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenDeletingNonexistentCampaign() {
        given()
            .queryParam("userId", 1)
        .when()
            .delete("/campaigns/999999")
        .then()
            .statusCode(404);
    }
}
```

- [ ] **Step 5: Run all campaign CRUD tests**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/campaign-service/campaign-service \
-Dtest="integration.**" --no-transfer-progress
```
Expected: All tests pass

- [ ] **Step 6: Commit**

```bash
git add services/campaign-service/campaign-service/src/test/java/integration/
git commit -m "feat: add campaign-service CRUD integration tests"
```

---

### Task 11: Campaign-service members integration tests

**Files:**
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignMemberIntegrationTest.java`

- [ ] **Step 1: Write CampaignMemberIntegrationTest**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignMemberIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldAddMemberToCampaign() {
        // Create campaign
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Member Test","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        // Add member
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"requesterId":1,"userId":2}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/members")
        .then()
            .statusCode(201);

        // Verify member listed
        var members = given()
        .when()
            .get("/campaigns/" + campaign.id() + "/members")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("$", Map.class);

        assertThat(members).isNotEmpty();
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldRemoveMemberFromCampaign() {
        // Create campaign
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Remove Test","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        // Add member
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"requesterId":1,"userId":2}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/members")
        .then()
            .statusCode(201);

        // Remove member
        given()
            .queryParam("requesterId", 1)
        .when()
            .delete("/campaigns/" + campaign.id() + "/members/2")
        .then()
            .statusCode(204);
    }
}
```

- [ ] **Step 2: Run member tests**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/campaign-service/campaign-service \
-Dtest=CampaignMemberIntegrationTest --no-transfer-progress
```
Expected: All tests pass

- [ ] **Step 3: Commit**

```bash
git add services/campaign-service/campaign-service/src/test/java/integration/
git commit -m "feat: add campaign-service member integration tests"
```

---

### Task 12: Campaign-service notes integration tests

**Files:**
- Create: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignNoteIntegrationTest.java`

- [ ] **Step 1: Write CampaignNoteIntegrationTest**

```java
package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignNoteIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldCreateAndListNotes() {
        // Create campaign
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Notes Campaign","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        // Create note
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Session 1","content":"The party entered the dungeon.","visibility":"PUBLIC"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(201);

        // List notes
        var notes = given()
            .queryParam("userId", 1)
        .when()
            .get("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("$", Map.class);

        assertThat(notes).hasSize(1);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateNote() {
        // Create campaign + note
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Update Notes","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        var noteId = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Original","content":"content","visibility":"PUBLIC"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(201)
            .extract().body().jsonPath().getLong("id");

        // Update note
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Updated Title","content":"Updated content","visibility":"PRIVATE"}
                """)
        .when()
            .put("/campaigns/" + campaign.id() + "/notes/" + noteId)
        .then()
            .statusCode(200);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldDeleteNote() {
        // Create campaign + note
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Delete Notes","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(201)
            .extract().as(CampaignViewModel.class);

        var noteId = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Delete Me","content":"content","visibility":"PUBLIC"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(201)
            .extract().body().jsonPath().getLong("id");

        // Delete
        given()
            .queryParam("userId", 1)
        .when()
            .delete("/campaigns/" + campaign.id() + "/notes/" + noteId)
        .then()
            .statusCode(204);
    }
}
```

- [ ] **Step 2: Run note tests**

Run:
```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/campaign-service/campaign-service \
-Dtest=CampaignNoteIntegrationTest --no-transfer-progress
```
Expected: All tests pass

- [ ] **Step 3: Commit**

```bash
git add services/campaign-service/campaign-service/src/test/java/integration/
git commit -m "feat: add campaign-service note integration tests"
```

---

## Phase 4: Remaining Services

Each service follows same pattern: (1) create test `application.properties`, (2) create entity providers, (3) write integration tests. Below are service-specific configs and key test classes.

### Task 13: Auth-service test configuration and integration tests

**Files:**
- Create: `services/auth-service/auth-service/src/test/resources/application.properties`
- Create: `services/auth-service/auth-service/src/test/java/integration/auth/LoginIntegrationTest.java`
- Create: `services/auth-service/auth-service/src/test/java/integration/auth/RefreshTokenIntegrationTest.java`

**Auth-service is special:** it GENERATES JWT tokens, so it needs real RSA keys and a WireMock server for user-service REST client calls.

- [ ] **Step 1: Create test application.properties for auth-service**

```properties
# --- Vault: disabled ---
quarkus.vault.url=

# --- Database ---
quarkus.datasource.db-kind=postgresql
db.username=quarkus
db.password=quarkus
auth-db.url=

# --- Flyway ---
quarkus.flyway.migrate-at-start=true
quarkus.flyway.baseline-on-migrate=true
quarkus.flyway.baseline-version=0
quarkus.flyway.locations=classpath:db/migration

# --- RabbitMQ ---
rabbitmq-host=localhost
rabbitmq-port=5672
rabbitmq-username=guest
rabbitmq-password=guest
rabbitmq-virtual-host=/

# --- Redis ---
quarkus.redis.hosts=redis://localhost:6379

# --- JWT: real RSA keys needed (auth-service generates tokens) ---
# Generate with: openssl genrsa -out private.pem 2048 && openssl rsa -in private.pem -pubout -out public.pem
# Inline the keys (replace newlines with \n)
smallrye.jwt.sign.key=REPLACE_WITH_TEST_PRIVATE_KEY_PEM
mp.jwt.verify.publickey=REPLACE_WITH_TEST_PUBLIC_KEY_PEM
mp.jwt.verify.issuer=test-issuer
jwt.issuer=test-issuer
jwt.access-token-expiry-seconds=3600
jwt.refresh-token-expiry-days=30
jwt.public-key=REPLACE_WITH_TEST_PUBLIC_KEY_PEM
jwt-config.issuer=test-issuer
jwt-config.access-token-expiry-seconds=3600
jwt-config.refresh-token-expiry-days=30

# --- REST Client: user-service (use WireMock) ---
quarkus.rest-client.rest_client_user_service.url=http://localhost:8790
rest-client.url=http://localhost:8790
service-token.rest_client_user_service_auth=test-service-token
service-token.rest_client_user_service_auth_http_header=x-service-token

# --- Password reset ---
password-reset.token-expiry-minutes=15

# --- OTP Login ---
otp-login.token-expiry-minutes=5

# --- Telemetry ---
quarkus.otel.enabled=false
quarkus.otel.sdk.disabled=true

# --- Logging ---
quarkus.log.console.json=false
```

**IMPORTANT:** Generate RSA key pair before running auth-service tests:
```bash
openssl genrsa -out /tmp/test-private.pem 2048
openssl rsa -in /tmp/test-private.pem -pubout -out /tmp/test-public.pem
```
Then paste the PEM contents (with `\n` replacing newlines) into the properties.

- [ ] **Step 2: Create WireMock for user-service**

Create `services/auth-service/auth-service/src/test/java/integration/resource/UserServiceWireMock.java`:

```java
package integration.resource;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class UserServiceWireMock implements QuarkusTestResourceLifecycleManager {

    private WireMockServer server;

    @Override
    public Map<String, String> start() {
        server = new WireMockServer(wireMockConfig().port(8790));
        server.start();
        return Map.of("quarkus.rest-client.rest_client_user_service.url", server.baseUrl());
    }

    @Override
    public void stop() {
        if (server != null) server.stop();
    }
}
```

Note: Add WireMock dependency to auth-service pom.xml:
```xml
<dependency>
    <groupId>org.wiremock</groupId>
    <artifactId>wiremock</artifactId>
    <version>3.12.1</version>
    <scope>test</scope>
</dependency>
```

- [ ] **Step 3: Write LoginIntegrationTest**

Auth-service login calls user-service to validate credentials. WireMock stubs that call.

```java
package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import integration.resource.UserServiceWireMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(UserServiceWireMock.class)
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class LoginIntegrationTest {

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldLoginSuccessfully() {
        // Stub user-service credentials validation
        stubFor(post(urlPathEqualTo("/users/credentials-validation"))
            .willReturn(okJson("""
                {"id":1,"username":"gandalf","email":"gandalf@shire.com","role":"PLAYER","active":true,"createdAt":"2026-01-01T00:00:00"}
                """)));

        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"gandalf","password":"YouShallNotPass1"}
                """)
        .when()
            .post("/auth/login-tokens")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath();

        assertThat(response.getString("accessToken")).isNotEmpty();
        assertThat(response.getString("refreshToken")).isNotEmpty();
        assertThat(response.getLong("userId")).isEqualTo(1L);
    }

    @Test
    void shouldReturn401ForInvalidCredentials() {
        // Stub user-service returning 401
        stubFor(post(urlPathEqualTo("/users/credentials-validation"))
            .willReturn(aResponse().withStatus(401)));

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"gandalf","password":"WrongPassword"}
                """)
        .when()
            .post("/auth/login-tokens")
        .then()
            .statusCode(401);
    }
}
```

- [ ] **Step 4: Run auth-service tests, fix issues, commit**

```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/auth-service/auth-service -Dtest="integration.**" --no-transfer-progress
```

```bash
git add services/auth-service/
git commit -m "feat: add auth-service integration tests with WireMock"
```

---

### Task 14: Chat-service test configuration and integration tests

**Files:**
- Create: `services/chat-service/chat-service/src/test/resources/application.properties`
- Create: `services/chat-service/chat-service/src/test/java/integration/chat/ConversationIntegrationTest.java`
- Create: `services/chat-service/chat-service/src/test/java/integration/chat/MessageIntegrationTest.java`

- [ ] **Step 1: Create test application.properties**

Same pattern as campaign-service. Key differences:
- DB property: `chat-db.url=` (check actual Vault path name in chat-service application.properties)
- No RabbitMQ if chat-service doesn't use messaging

- [ ] **Step 2: Write ConversationIntegrationTest**

Test: create conversation (`POST /api/chat/conversations`), find by ID, find by user, mark as read. All need `@TestSecurity(user = "1", roles = "PLAYER")`.

- [ ] **Step 3: Write MessageIntegrationTest**

Test: send message (`POST /api/chat/conversations/{id}/messages`), list messages. Need `@TestSecurity`.

- [ ] **Step 4: Run, fix, commit**

---

### Task 15: Notification-service test configuration and integration tests

**Files:**
- Create: `services/notification-service/notification-service/src/test/resources/application.properties`
- Create: `services/notification-service/notification-service/src/test/java/integration/email/EmailTemplateIntegrationTest.java`

Key points:
- Has RabbitMQ consumer (`email-requests-in`) — test with Emitter + Awaitility
- Has email template CRUD endpoints
- Mailer: set `quarkus.mailer.mock=true` for tests

- [ ] **Step 1: Create test config with `quarkus.mailer.mock=true`**
- [ ] **Step 2: Write EmailTemplateIntegrationTest (CRUD for templates)**
- [ ] **Step 3: Write EmailConsumerIntegrationTest (send message via Emitter, verify consumed)**
- [ ] **Step 4: Run, fix, commit**

---

### Task 16: Asset-service test configuration and integration tests

**Files:**
- Create: `services/asset-service/asset-service/src/test/resources/application.properties`
- Create: `services/asset-service/asset-service/src/test/java/integration/document/DocumentUploadIntegrationTest.java`

Key points:
- Uses MinIO for file storage — need Testcontainers MinIO or mock
- Has RabbitMQ producer and consumer
- File upload uses multipart form

- [ ] **Step 1: Create test config with MinIO DevServices or mock**
- [ ] **Step 2: Write DocumentUploadIntegrationTest (multipart upload)**
- [ ] **Step 3: Write DocumentListIntegrationTest**
- [ ] **Step 4: Run, fix, commit**

---

### Task 17: Character-service test configuration and integration tests

**Files:**
- Create: `services/character-service/character-service/src/test/resources/application.properties`
- Create: `services/character-service/character-service/src/test/java/integration/character/CharacterCreateIntegrationTest.java`
- Create: `services/character-service/character-service/src/test/java/integration/character/CharacterEntityProvider.java`

Key points:
- 16 entities — CharacterEntityProvider complex (needs abilities, skills, etc.)
- Import endpoint (`POST /characters/import`) — multipart
- Sheet download (`GET /characters/{id}/sheet`) — file response

- [ ] **Step 1: Create test config**
- [ ] **Step 2: Create CharacterEntityProvider (with related entities)**
- [ ] **Step 3: Write CharacterCreateIntegrationTest**
- [ ] **Step 4: Write CharacterFindAllIntegrationTest**
- [ ] **Step 5: Run, fix, commit**

---

### Task 18: Compendium-service test configuration and integration tests

**Files:**
- Create: `services/compendium-service/compendium-service/src/test/resources/application.properties`
- Create: `services/compendium-service/compendium-service/src/test/java/integration/compendium/SpellIntegrationTest.java`

Key points:
- 36 endpoints (18 entities x FindAll + FindById)
- Read-only data — entities populated via Flyway migrations (seed data)
- If seed data exists in migrations, no EntityProvider needed
- If no seed data, create entity providers per compendium type

- [ ] **Step 1: Create test config**
- [ ] **Step 2: Check if Flyway seeds compendium data**
- [ ] **Step 3: Write one representative test (SpellIntegrationTest) as template**
- [ ] **Step 4: Replicate pattern for remaining 17 compendium types**
- [ ] **Step 5: Run, fix, commit**

---

## Running All Integration Tests

After all tasks complete, run full integration test suite:

```bash
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -Dtest="integration.**" --no-transfer-progress --fail-at-end
```

Or per-service:
```bash
# Replace {service} with: user-service, campaign-service, auth-service, etc.
JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" \
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn" \
test -pl services/{service}/{service} -Dtest="integration.**" --no-transfer-progress
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Vault extension fails to start | Add `quarkus.vault.authentication.client-token=disabled` to test props |
| DevServices doesn't start PostgreSQL | Ensure Docker running. Check `quarkus.datasource.jdbc.url` is empty (not set to a value) |
| `@TestSecurity` not enforcing roles | Add `quarkus-test-security` dependency. Ensure security annotations present on ResourceImpl |
| RabbitMQ DevServices port conflict | Set `quarkus.rabbitmq.devservices.port=0` for random port |
| Test data leaking between tests | Add `@DeleteEntities` cleanup. Consider `@TestTransaction` |
| Flyway migrations fail on test DB | Check migration files in `src/main/resources/db/migration/` are compatible |
| `UserEntity` fields are private | Use setter methods instead of direct field access in EntityProviders |
| BCrypt not available | Add `io.quarkus:quarkus-elytron-security-common` dependency |
