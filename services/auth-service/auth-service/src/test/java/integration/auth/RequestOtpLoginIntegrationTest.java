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
class RequestOtpLoginIntegrationTest {

    @Test
    @DeleteEntities(from = OtpLoginEntity.class)
    void shouldRequestOtpLogin() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"gandalf@shire.com"}
                """)
        .when()
            .post("/auth/otp-login-requests")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(202),
                org.hamcrest.Matchers.equalTo(204)));
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"not-an-email"}
                """)
        .when()
            .post("/auth/otp-login-requests")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenEmailIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":""}
                """)
        .when()
            .post("/auth/otp-login-requests")
        .then()
            .statusCode(400);
    }
}
