package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class GetUserLlmConfigurationsIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldListUserLlmConfigurations() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/api/document-qa/llm/user-configurations")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/api/document-qa/llm/user-configurations")
        .then()
                .statusCode(401);
    }
}
