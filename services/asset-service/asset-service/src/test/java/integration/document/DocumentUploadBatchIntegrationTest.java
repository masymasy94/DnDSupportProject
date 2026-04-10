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
        // when / then
        given()
                .contentType("multipart/form-data")
        .when()
                .post("/api/assets/documents/batch")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .contentType("multipart/form-data")
        .when()
                .post("/api/assets/documents/batch")
        .then()
                .statusCode(401);
    }
}
