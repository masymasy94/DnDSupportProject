package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
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
class ValidateOtpLoginIntegrationTest {

    @Test
    @DeleteEntities(from = OtpLoginEntity.class)
    void shouldReturnErrorForInvalidOtpToken() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"gandalf@shire.com","otp":"invalid"}
                """)
        .when()
            .post("/auth/otp-login-tokens")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(401),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn400WhenEmailIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"otp":"123456"}
                """)
        .when()
            .post("/auth/otp-login-tokens")
        .then()
            .statusCode(400);
    }
}
