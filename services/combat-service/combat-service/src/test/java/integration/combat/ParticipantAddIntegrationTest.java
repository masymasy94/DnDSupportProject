package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

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
                .statusCode(400)
                .contentType(JSON);
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
