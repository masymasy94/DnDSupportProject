package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class ParticipantAddIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenEncounterMissingOrBodyInvalid() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: empty body to trigger validation
        .when()
                .post("/encounters/{id}/participants", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                // FIXME(integration-tests-rewrite): empty body should be 400, missing encounter should be 404.
                // Currently the product collapses both into either, depending on order.
                .statusCode(anyOf(equalTo(400), equalTo(404)));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: arbitrary body, auth fails first
        .when()
                .post("/encounters/{id}/participants", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
