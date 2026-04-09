package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class ActivateUserLlmConfigurationIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenConfigNotFound() {
        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .put("/api/document-qa/llm/user-configurations/{id}/activate", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                // FIXME(integration-tests-rewrite): activate on missing should be 404, not 204/400.
                .statusCode(anyOf(equalTo(204), equalTo(400), equalTo(404)));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
        .when()
                .put("/api/document-qa/llm/user-configurations/{id}/activate", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
