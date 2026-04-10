package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class EncounterCompleteIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenEncounterNotFound() {
        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .post("/encounters/{id}/complete", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
        .when()
                .post("/encounters/{id}/complete", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
