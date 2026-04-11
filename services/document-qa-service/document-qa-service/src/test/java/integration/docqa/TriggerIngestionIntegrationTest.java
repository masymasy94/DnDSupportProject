package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
class TriggerIngestionIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldRespondToTriggerRequest() {
        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .post("/api/document-qa/ingestion/{docId}", "some-doc") // hardcoded: arbitrary doc id
        .then()
                .statusCode(404)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType(JSON)
        .when()
                .post("/api/document-qa/ingestion/{docId}", "some-doc") // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
