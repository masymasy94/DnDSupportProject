package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class ActivateSystemLlmConfigurationIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "ADMIN")
    void shouldFailWhenConfigNotFound() {
        // when / then
        given()
                .contentType(JSON)
        .when()
                .put("/api/document-qa/llm/configurations/{id}/activate", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
        .when()
                .put("/api/document-qa/llm/configurations/{id}/activate", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
