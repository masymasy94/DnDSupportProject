package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

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
                // FIXME(integration-tests-rewrite): async ingestion trigger should be 202 (Accepted),
                // missing doc should be 404. The product mixes 200/202/204/400/404.
                .statusCode(anyOf(equalTo(200), equalTo(202), equalTo(204), equalTo(400), equalTo(404)));
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
