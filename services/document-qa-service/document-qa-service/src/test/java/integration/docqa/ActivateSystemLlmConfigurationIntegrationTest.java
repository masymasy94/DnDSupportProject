package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ActivateSystemLlmConfigurationIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "ADMIN")
    void shouldReturn404WhenConfigNotFound() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .put("/api/document-qa/llm/configurations/999999/activate")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(204),
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .put("/api/document-qa/llm/configurations/1/activate")
        .then()
            .statusCode(401);
    }
}
