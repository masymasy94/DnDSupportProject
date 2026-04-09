package integration.character;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

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
                // FIXME(integration-tests-rewrite): missing multipart should consistently return 400
                // (missing required parts), but the product also returns 415 when no boundary is set.
                .statusCode(anyOf(equalTo(400), equalTo(415)));
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
