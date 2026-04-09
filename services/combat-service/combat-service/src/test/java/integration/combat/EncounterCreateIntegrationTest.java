package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class EncounterCreateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenBodyIsEmpty() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/encounters")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/encounters")
        .then()
            .statusCode(401);
    }
}
