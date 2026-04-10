package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GetIngestionStatusIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenDocumentNotFound() {
        // when / then
        given()
        .when()
                .get("/api/document-qa/ingestion/{docId}/status", "nonexistent-doc") // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/document-qa/ingestion/{docId}/status", "some-doc") // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
