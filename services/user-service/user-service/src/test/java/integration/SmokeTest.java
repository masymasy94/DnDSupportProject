package integration;

import com.dndplatform.common.test.PostgreSQLTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
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
