package integration.character;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CharacterImportSheetIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenFileIsMissing() {
        given()
            .contentType("multipart/form-data")
        .when()
            .post("/characters/import-sheet")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(415)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType("multipart/form-data")
        .when()
            .post("/characters/import-sheet")
        .then()
            .statusCode(401);
    }
}
