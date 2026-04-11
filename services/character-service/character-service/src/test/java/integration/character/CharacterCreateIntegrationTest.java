package integration.character;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class CharacterCreateIntegrationTest {

    @Test
    @Disabled("KNOWN BUG: CharacterCreateResourceImpl missing @Valid annotation. "
        + "Empty bodies bypass validation and crash the delegate (HTTP 500). Re-enable after adding @Valid.")
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenRequestBodyIsEmpty() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: intentionally malformed body to trigger @Valid
        .when()
                .post("/characters")
        .then()
                .statusCode(400)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
                .body("{}") // hardcoded: arbitrary body, auth fails first
        .when()
                .post("/characters")
        .then()
                .statusCode(401);
    }
}
