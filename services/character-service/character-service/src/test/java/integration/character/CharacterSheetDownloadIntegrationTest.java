package integration.character;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CharacterSheetDownloadIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenCharacterNotFound() {
        given()
        .when()
            .get("/characters/999999/sheet")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .get("/characters/1/sheet")
        .then()
            .statusCode(401);
    }
}
