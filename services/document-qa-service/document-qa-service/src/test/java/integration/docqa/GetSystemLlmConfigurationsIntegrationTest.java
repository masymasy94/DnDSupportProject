package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GetSystemLlmConfigurationsIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "ADMIN")
    void shouldListSystemLlmConfigurations() {
        given()
        .when()
            .get("/api/document-qa/llm/configurations")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn403ForNonAdmin() {
        given()
        .when()
            .get("/api/document-qa/llm/configurations")
        .then()
            .statusCode(403);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .get("/api/document-qa/llm/configurations")
        .then()
            .statusCode(401);
    }
}
