package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ParticipantDeleteIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenNotFound() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .delete("/encounters/{eid}/participants/{pid}", 999_999L, 999_999L) // hardcoded: ids outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .delete("/encounters/{eid}/participants/{pid}", 1L, 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
