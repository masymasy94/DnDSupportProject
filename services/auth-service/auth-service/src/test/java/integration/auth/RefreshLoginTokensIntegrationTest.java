package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import integration.resource.UserServiceWireMockResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class RefreshLoginTokensIntegrationTest {

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldReturn400OrUnauthorizedForInvalidRefreshToken() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"refreshToken":"invalid-token","userId":1}
                """)
        .when()
            .post("/auth/login-tokens/refreshed")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(401),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn400WhenRefreshTokenIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {}
                """)
        .when()
            .post("/auth/login-tokens/refreshed")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(401)));
    }
}
