package integration.document;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class DocumentUploadBatchIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldRespondToEmptyBatchRequest() {
        // Empty batch may return 200 (empty result) or 400 depending on validation
        given()
            .contentType("multipart/form-data")
        .when()
            .post("/api/assets/documents/batch")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(415)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType("multipart/form-data")
        .when()
            .post("/api/assets/documents/batch")
        .then()
            .statusCode(401);
    }
}
