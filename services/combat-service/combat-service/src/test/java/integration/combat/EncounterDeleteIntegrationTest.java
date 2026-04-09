package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class EncounterDeleteIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenEncounterNotFound() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .delete("/encounters/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                // FIXME(integration-tests-rewrite): DELETE on missing should consistently return 404 or 204
                // (idempotent), not 400.
                .statusCode(anyOf(equalTo(400), equalTo(404)));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .delete("/encounters/{id}", 1L) // hardcoded: arbitrary id, auth fails first
        .then()
                .statusCode(401);
    }
}
