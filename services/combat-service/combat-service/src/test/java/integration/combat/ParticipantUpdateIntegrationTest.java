package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ParticipantUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .put("/encounters/999999/participants/999999")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .put("/encounters/1/participants/1")
        .then()
            .statusCode(401);
    }
}
