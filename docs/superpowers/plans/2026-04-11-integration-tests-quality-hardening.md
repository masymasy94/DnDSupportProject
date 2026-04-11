# Integration Tests Quality Hardening

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
>
> **Spec:** This plan implements 7 concrete improvements identified during code review of the `feature/integration` branch. See the review summary in the previous conversation for rationale.

**Goal:** Harden integration test quality across all services: fix framework generics, add WireMock verification, strengthen assertions, add soft assertions, validate error Content-Type, and migrate from H2 to PostgreSQL Dev Services.

**Architecture:** Phase 0 fixes the common-test framework (1 task). Phase 1 hardens WireMock usage in auth-service (3 tasks). Phase 2 strengthens assertions cross-service (3 tasks). Phase 3 migrates all test databases from H2 to PostgreSQL via Quarkus Dev Services (1 task). Each phase produces one commit.

**Tech Stack:** Quarkus 3.31.4, JUnit 5, REST-Assured, AssertJ (SoftAssertions), WireMock, Instancio, Quarkus Dev Services (Testcontainers).

**Maven invocation:** Maven not on PATH. Use:
```bash
bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn"
```
Wherever this plan says `mvn ...`, substitute the absolute path above.

---

## Phase 0 — Framework Fix (`common-test`)

### Task 0.1: Fix `@InjectRandom` to preserve generic type information

**Files:**
- Modify: `services/common-test/src/main/java/com/dndplatform/common/test/RandomExtension.java:31`

Currently `@InjectRandom List<String>` generates `List<Object>` because `field.getType()` returns the raw `List` class. Fix: use `field.getGenericType()`.

- [ ] **Step 1: Write a failing test**

Add a new test to the existing test file `services/common-test/src/test/java/com/dndplatform/common/test/RandomExtensionFieldInjectionTest.java`:

```java
@InjectRandom
private java.util.List<String> randomStringList;

@Test
void shouldInjectRandomGenericListField() {
    assertThat(randomStringList).isNotNull().isNotEmpty();
    assertThat(randomStringList.get(0)).isInstanceOf(String.class);
}
```

- [ ] **Step 2: Run to verify it fails**

```bash
mvn -pl services/common-test test -Dtest=RandomExtensionFieldInjectionTest#shouldInjectRandomGenericListField
```

Expected: FAIL — `randomStringList.get(0)` is NOT a `String` (it's some random Object).

- [ ] **Step 3: Fix `RandomExtension.beforeEach()`**

In `services/common-test/src/main/java/com/dndplatform/common/test/RandomExtension.java`, change line 31:

```java
// Before:
Object value = Instancio.create(field.getType());

// After:
Object value = Instancio.create(field.getGenericType());
```

Also fix `resolveParameter` (line 20) for consistency:

```java
// Before:
return Instancio.create(paramCtx.getParameter().getType());

// After:
return Instancio.create(paramCtx.getParameter().getParameterizedType());
```

- [ ] **Step 4: Run to verify it passes**

```bash
mvn -pl services/common-test test
```

Expected: ALL tests pass, including the new generic test.

- [ ] **Step 5: Commit**

```bash
git add services/common-test/
git commit -m "fix(common-test): preserve generic type info in @InjectRandom and @Random

RandomExtension now uses field.getGenericType() instead of
field.getType(), so @InjectRandom List<String> correctly generates
List<String> instead of List<Object>. Same fix for @Random parameters.

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

---

## Phase 1 — WireMock Hardening (`auth-service`)

### Task 1.1: Add `verify()` to auth-service WireMock tests

**Files:**
- Modify: `services/auth-service/auth-service/src/test/java/integration/auth/CreateLoginTokensIntegrationTest.java`
- Modify: `services/auth-service/auth-service/src/test/java/integration/auth/RequestPasswordResetIntegrationTest.java`
- Modify: `services/auth-service/auth-service/src/test/java/integration/auth/RequestOtpLoginIntegrationTest.java`
- Modify: `services/auth-service/auth-service/src/test/java/integration/auth/ResetPasswordIntegrationTest.java`

Auth-service tests configure WireMock stubs but never call `verify()`. If the code stops calling user-service, tests still pass. Add verification.

- [ ] **Step 1: Add WireMock verify to `CreateLoginTokensIntegrationTest.shouldCreateLoginTokens()`**

Add import and get server reference:

```java
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
```

After the REST-assured assertion block, add:

```java
        // then — verify user-service was called with correct credentials
        UserServiceWireMockResource.getServer().verify(
                postRequestedFor(urlPathMatching("/users/credentials-validation/?"))
                        .withRequestBody(matchingJsonPath("$.username", equalTo("gandalf"))));
```

- [ ] **Step 2: Add WireMock verify to `RequestPasswordResetIntegrationTest.shouldRequestPasswordReset()`**

After the REST-assured assertion block:

```java
        // then — verify email lookup was called
        UserServiceWireMockResource.getServer().verify(
                postRequestedFor(urlPathMatching("/users/email-lookup/?"))
                        .withRequestBody(matchingJsonPath("$.email", equalTo("gandalf@shire.com"))));
```

- [ ] **Step 3: Add WireMock verify to `RequestOtpLoginIntegrationTest.shouldRequestOtpLogin()`**

After the REST-assured assertion block:

```java
        // then — verify email lookup was called
        UserServiceWireMockResource.getServer().verify(
                postRequestedFor(urlPathMatching("/users/email-lookup/?"))
                        .withRequestBody(matchingJsonPath("$.email", equalTo("gandalf@shire.com"))));
```

- [ ] **Step 4: Run auth-service tests**

```bash
mvn test -pl services/auth-service/auth-service
```

Expected: ALL tests pass.

### Task 1.2: Add WireMock error propagation tests

**Files:**
- Modify: `services/auth-service/auth-service/src/test/java/integration/auth/CreateLoginTokensIntegrationTest.java`
- Modify: `services/auth-service/auth-service/src/test/java/integration/resource/UserServiceWireMockResource.java`

No test verifies what happens when user-service is down or returns 500. Add error propagation tests.

- [ ] **Step 1: Add `resetAll()` + per-test stub pattern to `CreateLoginTokensIntegrationTest`**

Add a new test method:

```java
    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldFailWhenUserServiceReturnsServerError() throws JsonProcessingException {
        // given
        var server = UserServiceWireMockResource.getServer();
        server.stubFor(post(urlPathMatching("/users/credentials-validation/?"))
                .willReturn(aResponse().withStatus(500)));

        var request = CreateLoginTokensViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("gandalf") // hardcoded: matches default stub, overridden above
                .withPassword("YouShallNotPass1") // hardcoded: arbitrary
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/login-tokens")
        .then()
                .statusCode(500);

        // cleanup: restore default stub
        server.resetToDefaultMappings();
    }
```

Add required imports:

```java
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
```

- [ ] **Step 2: Run auth-service tests**

```bash
mvn test -pl services/auth-service/auth-service
```

Expected: ALL tests pass. The `statusCode(500)` might need adjusting to the actual response code if auth-service wraps the error differently. Read the test output and fix accordingly.

### Task 1.3: Enable WireMock near-miss tracking in `UserServiceWireMockResource`

**Files:**
- Modify: `services/auth-service/auth-service/src/test/java/integration/resource/UserServiceWireMockResource.java`

If code sends a request that doesn't match any stub, WireMock silently returns 404. Enable near-miss logging to catch unmatched requests.

- [ ] **Step 1: Add catch-all stub for unmatched requests**

In `UserServiceWireMockResource.start()`, after the existing stubs, add:

```java
        // Catch-all: any unmatched request returns 501 with a descriptive body
        // so test failures are obvious instead of a silent 404
        server.stubFor(any(anyUrl())
                .atPriority(Integer.MAX_VALUE)
                .willReturn(aResponse()
                        .withStatus(501)
                        .withBody("WireMock: no stub matched this request")));
```

- [ ] **Step 2: Run auth-service tests**

```bash
mvn test -pl services/auth-service/auth-service
```

Expected: ALL tests pass (no unmatched requests).

- [ ] **Step 3: Commit Phase 1**

```bash
git add services/auth-service/
git commit -m "test(auth-service): add WireMock verify(), error propagation, and catch-all stub

- Happy-path tests now verify() that user-service was called with correct body
- New test for user-service 500 error propagation
- Catch-all stub returns 501 for unmatched requests instead of silent 404

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

---

## Phase 2 — Assertion Hardening (cross-service)

### Task 2.1: Strengthen happy-path response body assertions

**Files:**
- Modify: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignMemberAddIntegrationTest.java`
- Modify: `services/campaign-service/campaign-service/src/test/java/integration/campaign/CampaignNoteCreateIntegrationTest.java`
- Modify: `services/chat-service/chat-service/src/test/java/integration/chat/MessageFindByConversationIntegrationTest.java`
- Modify: `services/chat-service/chat-service/src/test/java/integration/chat/ConversationUpdateReadByIdIntegrationTest.java`

These tests verify only `statusCode()` on happy paths without checking response body. If endpoint returns correct status but wrong/empty body, test passes silently.

- [ ] **Step 1: Strengthen `CampaignMemberAddIntegrationTest.shouldAddMemberToCampaign()`**

Change the `// when / then` block (lines 72-78) to extract and verify the response:

```java
        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(addMemberRequest))
        .when()
                .post("/campaigns/{id}/members", campaign.id())
        .then()
                .statusCode(201)
                .contentType(JSON)
                .body("userId", equalTo(2))
                .body("campaignId", equalTo(campaign.id().intValue()));
```

Add import: `import static org.hamcrest.Matchers.equalTo;`

- [ ] **Step 2: Strengthen `CampaignNoteCreateIntegrationTest.shouldCreateNote()`**

Change lines 74-82 to verify more fields:

```java
        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(noteRequest))
        .when()
                .post("/campaigns/{id}/notes", campaign.id())
        .then()
                .statusCode(201)
                .contentType(JSON)
                .body("title", equalTo("Session 1"))
                .body("content", equalTo("The party entered the dungeon."))
                .body("visibility", equalTo("PUBLIC"))
                .body("id", notNullValue());
```

Add import: `import static org.hamcrest.Matchers.notNullValue;`

- [ ] **Step 3: Strengthen `MessageFindByConversationIntegrationTest.shouldListMessagesInConversation()`**

Change lines 39-47 to verify response structure:

```java
        // when / then
        given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("pageSize", 20) // hardcoded: arbitrary page size
        .when()
                .get("/api/chat/conversations/{id}/messages", conversationId)
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", greaterThanOrEqualTo(0));
```

Add import: `import static org.hamcrest.Matchers.greaterThanOrEqualTo;`

- [ ] **Step 4: Strengthen `ConversationUpdateReadByIdIntegrationTest.shouldMarkConversationAsRead()`**

After the PUT 204, add a GET to verify the read status was actually updated:

```java
        // when
        given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .put("/api/chat/conversations/{id}/read", conversationId)
        .then()
                .statusCode(204);

        // then — verify read status via GET
        given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .get("/api/chat/conversations/{id}", conversationId)
        .then()
                .statusCode(200)
                .contentType(JSON);
```

Add import: `import static io.restassured.http.ContentType.JSON;`

- [ ] **Step 5: Run affected services**

```bash
mvn test -pl services/campaign-service/campaign-service && mvn test -pl services/chat-service/chat-service
```

Expected: ALL tests pass. If any assertion fails because the response body shape doesn't match, read the actual response and adjust the Hamcrest matchers to match the actual field names.

### Task 2.2: Add soft assertions to tests with 3+ sequential `assertThat()` calls

**Files:**
- Modify: `services/user-service/user-service/src/test/java/integration/user/UserRegisterIntegrationTest.java`
- Modify: `services/user-service/user-service/src/test/java/integration/user/UserFindByIdIntegrationTest.java`
- Modify: `services/user-service/user-service/src/test/java/integration/user/UserFindByEmailIntegrationTest.java`
- Modify: `services/user-service/user-service/src/test/java/integration/user/UserCredentialsValidateIntegrationTest.java`
- Modify: `services/user-service/user-service/src/test/java/integration/user/UserSearchIntegrationTest.java`
- Modify: `services/user-service/user-service/src/test/java/integration/user/UserUpdatePasswordIntegrationTest.java`
- Modify: `services/auth-service/auth-service/src/test/java/integration/auth/CreateLoginTokensIntegrationTest.java`

With hard assertions, the first failure hides other broken fields. Soft assertions report ALL failures at once.

- [ ] **Step 1: Convert `UserRegisterIntegrationTest.shouldRegisterNewUser()` to soft assertions**

Replace the `// then` block (lines 68-74):

```java
        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.id()).isNotNull();
            softly.assertThat(response.username()).isEqualTo(username);
            softly.assertThat(response.email()).isEqualTo("user-" + username + "@example.com");
            softly.assertThat(response.role()).isEqualTo("PLAYER");
            softly.assertThat(response.active()).isTrue();
            softly.assertThat(response.createdAt()).isNotNull();
        });
```

Replace import:
```java
// Before:
import static org.assertj.core.api.Assertions.assertThat;
// After:
import org.assertj.core.api.SoftAssertions;
```

- [ ] **Step 2: Convert `UserFindByIdIntegrationTest.shouldFindUserById()` to soft assertions**

Replace the `// then` block (lines 67-69):

```java
        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(registered.id());
            softly.assertThat(response.username()).isEqualTo(safeUsername);
            softly.assertThat(response.email()).isEqualTo(safeUsername + "@example.com");
        });
```

Replace import: same pattern as Step 1.

- [ ] **Step 3: Apply same pattern to remaining user-service tests**

For each file listed above that has 3+ sequential `assertThat()` calls in a `// then` block:
1. Replace `import static org.assertj.core.api.Assertions.assertThat;` with `import org.assertj.core.api.SoftAssertions;`
2. Wrap the assertions in `SoftAssertions.assertSoftly(softly -> { ... });`
3. Change `assertThat(` to `softly.assertThat(`

If a file also uses single `assertThat()` in other test methods, keep both imports.

- [ ] **Step 4: Run user-service tests**

```bash
mvn test -pl services/user-service/user-service
```

Expected: ALL tests pass.

### Task 2.3: Add `contentType(JSON)` to error response assertions

**Files:** All integration test files that have error-path tests (400, 404, 409) without `.contentType(JSON)`.

Error-path tests verify only status code. If an error handler returns HTML instead of JSON, the test misses the API contract violation.

- [ ] **Step 1: Find all error-path tests missing contentType**

```bash
grep -rn '\.statusCode(4[0-9][0-9])' services/*/*/src/test/java/integration/ | grep -v contentType | head -50
```

This shows lines where a 4xx status is asserted without a `contentType` assertion nearby.

- [ ] **Step 2: Add `.contentType(JSON)` after each 4xx status code assertion**

For each line found in Step 1, add `.contentType(JSON)` after `.statusCode(4xx)`. Example:

```java
// Before:
        .then()
                .statusCode(400);

// After:
        .then()
                .statusCode(400)
                .contentType(JSON);
```

Ensure `import static io.restassured.http.ContentType.JSON;` exists in each file.

**Exception:** Do NOT add `contentType(JSON)` to `statusCode(401)` tests. Quarkus returns 401 with no body (empty response), so content-type may not be set. Only add to 400, 404, 409 responses.

- [ ] **Step 3: Run all service tests**

```bash
mvn test -pl services/user-service/user-service && \
mvn test -pl services/campaign-service/campaign-service && \
mvn test -pl services/chat-service/chat-service && \
mvn test -pl services/auth-service/auth-service && \
mvn test -pl services/notification-service/notification-service
```

Expected: ALL tests pass. If a test fails because the endpoint returns a non-JSON error, leave that test WITHOUT `contentType(JSON)` and add a comment: `// no contentType: endpoint returns non-JSON error body`.

- [ ] **Step 4: Commit Phase 2**

```bash
git add services/
git commit -m "test: harden assertions across integration tests

- Strengthen happy-path response body assertions (campaign, chat)
- Add SoftAssertions for tests with 3+ assertions (user-service)
- Add contentType(JSON) to 400/404/409 error responses

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

---

## Phase 3 — Dev Services Migration

### Task 3.1: Migrate from H2 to PostgreSQL Dev Services

**Files:**
- Modify: 10 test `application.properties` files (one per service)
- Modify: 10 service `pom.xml` files (remove H2 test dependency if present)

H2 can't catch PostgreSQL-specific bugs (jsonb, ON CONFLICT, sequence behavior). Quarkus Dev Services auto-starts a real PostgreSQL container via Testcontainers. Requires Docker running.

**Pre-requisite:** Docker must be available on the machine running tests.

- [ ] **Step 1: Update `user-service` test `application.properties` (reference implementation)**

Replace the database section of `services/user-service/user-service/src/test/resources/application.properties`:

```properties
# --- Database: PostgreSQL via Dev Services (requires Docker) ---
quarkus.datasource.db-kind=postgresql
quarkus.datasource.devservices.enabled=true
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true
```

Remove these lines:
```properties
# REMOVE:
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH
quarkus.datasource.username=sa
quarkus.datasource.password=

# REMOVE (no longer needed, Dev Services configures):
db.username=sa
db.password=
user-db.url=jdbc:h2:mem:testdb
```

Keep: Flyway disabled (`quarkus.flyway.migrate-at-start=false`), since we use Hibernate schema generation for tests.

- [ ] **Step 2: Check H2 dependency in user-service pom.xml**

```bash
grep -n 'h2' services/user-service/user-service/pom.xml
```

If H2 is a test dependency, remove it. The PostgreSQL JDBC driver should already be present (used by production code). If not, add:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>
```

- [ ] **Step 3: Run user-service tests**

```bash
mvn test -pl services/user-service/user-service
```

Expected: ALL tests pass. Docker should auto-start a PostgreSQL container. First run will be slower (container image pull).

If it fails with "Cannot find a container" or similar, ensure Docker is running.

- [ ] **Step 4: Apply same changes to remaining 9 services**

For each service in this order:
1. `notification-service`
2. `asset-service`
3. `character-service`
4. `chat-service`
5. `auth-service`
6. `combat-service`
7. `document-qa-service`
8. `campaign-service`
9. `compendium-service`

Apply the same transformation to each `application.properties`:
- Change `quarkus.datasource.db-kind=h2` → `quarkus.datasource.db-kind=postgresql`
- Add `quarkus.datasource.devservices.enabled=true`
- Remove `quarkus.datasource.jdbc.url=jdbc:h2:mem:...`
- Remove `quarkus.datasource.username=sa` and `quarkus.datasource.password=`
- Remove any `db.username`, `db.password`, `*-db.url` overrides that pointed to H2
- Keep `quarkus.hibernate-orm.database.generation=drop-and-create`
- Keep `quarkus.flyway.migrate-at-start=false`

**Service-specific notes:**
- `auth-service`: also has `auth-db.url` — remove it
- `chat-service`: also has `chat-db.url` — remove it
- `campaign-service`: also has `campaign-db.url` — remove it
- `search-service`: has `search-db.url` — remove it (search-service has no integration tests but keep config consistent)

Run each service's tests after applying:
```bash
mvn test -pl services/<service>/<service>
```

- [ ] **Step 5: Remove H2 dependencies from pom.xml files**

```bash
grep -rn 'quarkus-jdbc-h2\|com.h2database' services/*/pom.xml services/*/*/pom.xml
```

Remove any H2 JDBC dependency found. If a service only has `quarkus-jdbc-postgresql`, no change needed.

- [ ] **Step 6: Full build**

```bash
mvn clean test
```

Expected: ALL tests pass across all services.

- [ ] **Step 7: Commit Phase 3**

```bash
git add services/
git commit -m "test: migrate from H2 to PostgreSQL Dev Services

Replace H2 in-memory database with real PostgreSQL containers via
Quarkus Dev Services (Testcontainers). Tests now run against the same
database engine as production, catching dialect-specific bugs that
H2 MODE=PostgreSQL cannot reproduce.

Requires Docker to be running for test execution.

Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>"
```

---

## Verification

After all phases complete:

1. **Full build from root:**
   ```bash
   mvn clean test
   ```
   All tests green.

2. **Grep checks:**
   ```bash
   # No remaining field.getType() in RandomExtension
   grep "field.getType()" services/common-test/src/main/java/com/dndplatform/common/test/RandomExtension.java
   # Expected: 0 results

   # No H2 in test application.properties
   grep "db-kind=h2" services/*/*/src/test/resources/application.properties
   # Expected: 0 results

   # WireMock verify exists in auth-service tests
   grep -rn "\.verify(" services/auth-service/auth-service/src/test/java/integration/
   # Expected: 3+ results
   ```

3. **Code review:** Invoke `superpowers:requesting-code-review` on the full diff.
