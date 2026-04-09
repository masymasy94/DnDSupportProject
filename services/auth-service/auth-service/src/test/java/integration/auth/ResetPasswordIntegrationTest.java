package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntity;
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
class ResetPasswordIntegrationTest {

    @Test
    @DeleteEntities(from = PasswordResetEntity.class)
    void shouldReturnErrorForInvalidResetToken() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"token":"invalid-token","newPassword":"NewPassword1"}
                """)
        .when()
            .put("/auth/password-resets")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(401),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn400WhenTokenIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"newPassword":"NewPassword1"}
                """)
        .when()
            .put("/auth/password-resets")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenNewPasswordIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"token":"some-token"}
                """)
        .when()
            .put("/auth/password-resets")
        .then()
            .statusCode(400);
    }
}
