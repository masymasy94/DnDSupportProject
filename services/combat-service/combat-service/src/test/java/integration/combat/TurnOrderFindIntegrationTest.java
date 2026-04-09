package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TurnOrderFindIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldRespondWithEmptyOrNotFound() {
        // Service returns empty list (200) for nonexistent encounter
        given()
        .when()
            .get("/encounters/999999/turns")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .get("/encounters/1/turns")
        .then()
            .statusCode(401);
    }
}
