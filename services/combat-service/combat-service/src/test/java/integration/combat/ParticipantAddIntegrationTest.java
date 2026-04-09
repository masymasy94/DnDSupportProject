package integration.combat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ParticipantAddIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400or404WhenEncounterOrBodyInvalid() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/encounters/999999/participants")
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
            .post("/encounters/1/participants")
        .then()
            .statusCode(401);
    }
}
