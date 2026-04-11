package integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class SmokeTest {

    @Test
    void shouldBeHealthy() {
        given()
            .when()
                .get("/q/health")
            .then()
                .statusCode(200);
    }
}
