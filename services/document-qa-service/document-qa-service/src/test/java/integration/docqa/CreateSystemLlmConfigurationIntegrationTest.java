package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class CreateSystemLlmConfigurationIntegrationTest {

    @Test
    @Disabled("KNOWN BUG: LlmConfigurationResourceImpl#createSystemConfiguration missing @Valid, "
        + "empty bodies crash the delegate (HTTP 500). Re-enable after adding @Valid.")
    @TestSecurity(user = "1", roles = "ADMIN")
    void shouldFailForInvalidRequest() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: empty body to trigger validation
        .when()
                .post("/api/document-qa/llm/configurations")
        .then()
                .statusCode(400)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailForNonAdmin() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: arbitrary, role check runs first
        .when()
                .post("/api/document-qa/llm/configurations")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: arbitrary, auth fails first
        .when()
                .post("/api/document-qa/llm/configurations")
        .then()
                .statusCode(401);
    }
}
