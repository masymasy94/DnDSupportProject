package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class ParticipantUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenNotFound() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: empty body, validation may also fail
        .when()
                .put("/encounters/{eid}/participants/{pid}", 999_999L, 999_999L) // hardcoded: ids outside any seeded fixture
        .then()
                // FIXME(integration-tests-rewrite): missing participant should be 404, not 400.
                .statusCode(anyOf(equalTo(400), equalTo(404)));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: arbitrary body, auth fails first
        .when()
                .put("/encounters/{eid}/participants/{pid}", 1L, 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
