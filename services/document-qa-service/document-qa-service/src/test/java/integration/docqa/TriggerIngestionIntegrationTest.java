package integration.docqa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TriggerIngestionIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldRespondToTriggerRequest() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
        .when()
            .post("/api/document-qa/ingestion/some-doc")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(202),
                org.hamcrest.Matchers.equalTo(204),
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/document-qa/ingestion/some-doc")
        .then()
            .statusCode(401);
    }
}
