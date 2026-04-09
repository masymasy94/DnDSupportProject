package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class GetSystemLlmConfigurationsIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "ADMIN")
    void shouldListSystemLlmConfigurations() {
        // when / then
        given()
        .when()
                .get("/api/document-qa/llm/configurations")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailForNonAdmin() {
        // when / then
        given()
        .when()
                .get("/api/document-qa/llm/configurations")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/document-qa/llm/configurations")
        .then()
                .statusCode(401);
    }
}
