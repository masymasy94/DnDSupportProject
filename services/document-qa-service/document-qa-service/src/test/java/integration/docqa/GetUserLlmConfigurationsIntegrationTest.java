package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GetUserLlmConfigurationsIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldListUserLlmConfigurations() {
        given()
            .queryParam("userId", 1)
        .when()
            .get("/api/document-qa/llm/user-configurations")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("userId", 1)
        .when()
            .get("/api/document-qa/llm/user-configurations")
        .then()
            .statusCode(401);
    }
}
