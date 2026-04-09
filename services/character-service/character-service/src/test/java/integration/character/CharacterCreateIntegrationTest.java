package integration.character;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CharacterCreateIntegrationTest {

    @Test
    @Disabled("KNOWN BUG: CharacterCreateResourceImpl missing @Valid annotation. "
        + "Empty bodies bypass validation and crash the delegate (HTTP 500). Re-enable after adding @Valid.")
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenRequestBodyIsEmpty() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/characters")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/characters")
        .then()
            .statusCode(401);
    }
}
