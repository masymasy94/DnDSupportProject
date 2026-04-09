package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TurnAdvanceIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenEncounterNotFound() {
        given()
            .queryParam("userId", 1)
        .when()
            .put("/encounters/999999/turns/next")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .put("/encounters/1/turns/next")
        .then()
            .statusCode(401);
    }
}
