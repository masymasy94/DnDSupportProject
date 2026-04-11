package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

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
                .statusCode(404)
                .contentType(JSON);
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
