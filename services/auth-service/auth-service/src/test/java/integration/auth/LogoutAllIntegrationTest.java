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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class LogoutAllIntegrationTest {

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldLogoutAllSessions() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary user id
        .when()
                .delete("/auth/login-tokens")
        .then()
                // FIXME(integration-tests-rewrite): DELETE all should consistently return 204 (no body),
                // not a mix of 200/204.
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }
}
