package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModelBuilder;
import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import integration.resource.UserServiceWireMockResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class RequestOtpLoginIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private RequestOtpLoginViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = OtpLoginEntity.class)
    void shouldRequestOtpLogin() throws JsonProcessingException {
        // given
        var request = RequestOtpLoginViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("gandalf@shire.com") // hardcoded: matches the email-lookup stub in UserServiceWireMockResource
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/otp-login-requests")
        .then()
                .statusCode(202);
    }

    @Test
    void shouldFailWhenEmailIsInvalid() throws JsonProcessingException {
        // given
        var request = RequestOtpLoginViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("not-an-email") // hardcoded: triggers @Email validation
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/otp-login-requests")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenEmailIsBlank() throws JsonProcessingException {
        // given
        var request = RequestOtpLoginViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("") // hardcoded: triggers @NotBlank
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/otp-login-requests")
        .then()
                .statusCode(400);
    }
}
