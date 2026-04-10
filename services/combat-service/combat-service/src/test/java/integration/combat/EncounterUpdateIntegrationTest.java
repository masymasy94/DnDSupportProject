package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class EncounterUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenEncounterNotFound() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: empty body to test missing encounter lookup
        .when()
                .put("/encounters/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: arbitrary body, auth fails first
        .when()
                .put("/encounters/{id}", 1L) // hardcoded: arbitrary id, auth fails first
        .then()
                .statusCode(401);
    }
}
