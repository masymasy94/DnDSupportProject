package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class TurnOrderFindIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldRespondWithEmptyOrNotFound() {
        // when / then
        given()
        .when()
                .get("/encounters/{id}/turns", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/encounters/{id}/turns", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
