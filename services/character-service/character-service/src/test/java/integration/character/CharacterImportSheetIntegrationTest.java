package integration.character;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CharacterImportSheetIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenFileIsMissing() {
        // when / then
        given()
                .contentType("multipart/form-data")
        .when()
                .post("/characters/import-sheet")
        .then()
                .statusCode(400)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType("multipart/form-data")
        .when()
                .post("/characters/import-sheet")
        .then()
                .statusCode(401);
    }
}
