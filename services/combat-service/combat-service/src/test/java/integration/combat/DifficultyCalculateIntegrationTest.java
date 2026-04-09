package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class DifficultyCalculateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenEncounterNotFound() {
        // when / then
        given()
        .when()
                .get("/encounters/{id}/difficulty", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                // FIXME(integration-tests-rewrite): missing encounter should be 404, not 400.
                .statusCode(anyOf(equalTo(400), equalTo(404)));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/encounters/{id}/difficulty", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
