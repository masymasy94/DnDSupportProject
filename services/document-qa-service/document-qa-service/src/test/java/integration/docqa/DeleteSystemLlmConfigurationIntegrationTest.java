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
        given()
        .when()
            .delete("/api/document-qa/llm/configurations/999999")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(204),
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .delete("/api/document-qa/llm/configurations/1")
        .then()
            .statusCode(401);
    }
}
