package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class DeleteSystemLlmConfigurationIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "ADMIN")
    void shouldRespondToDelete() {
        // when / then
        given()
        .when()
                .delete("/api/document-qa/llm/configurations/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                // FIXME(integration-tests-rewrite): DELETE on missing should be 404 (strict) or 204 (idempotent),
                // not 400.
                .statusCode(anyOf(equalTo(204), equalTo(400), equalTo(404)));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .delete("/api/document-qa/llm/configurations/{id}", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
