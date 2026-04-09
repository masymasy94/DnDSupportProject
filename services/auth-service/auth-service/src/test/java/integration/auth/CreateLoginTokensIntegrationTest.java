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
class CreateLoginTokensIntegrationTest {

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldCreateLoginTokens() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"gandalf","password":"YouShallNotPass1"}
                """)
        .when()
            .post("/auth/login-tokens")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(201)))
            .contentType(ContentType.JSON)
            .body("accessToken", org.hamcrest.Matchers.notNullValue())
            .body("refreshToken", org.hamcrest.Matchers.notNullValue());
    }

    @Test
    void shouldReturn400WhenUsernameIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"password":"YouShallNotPass1"}
                """)
        .when()
            .post("/auth/login-tokens")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(401)));
    }
}
