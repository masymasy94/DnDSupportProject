package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import integration.resource.UserServiceWireMockResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class LogoutIntegrationTest {

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldLogoutWithRefreshToken() {
        // when / then
        // Logout by refresh token. With no token in DB, the product accepts and returns 200/204,
        // or returns 404 depending on its delete-strict policy.
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary user id
        .when()
                .delete("/auth/login-tokens/{token}", "some-refresh-token") // hardcoded: arbitrary token, may not exist
        .then()
                .statusCode(204);
    }
}
