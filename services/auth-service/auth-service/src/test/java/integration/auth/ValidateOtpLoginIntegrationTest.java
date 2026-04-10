package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModelBuilder;
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
class ValidateOtpLoginIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private ValidateOtpLoginViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = OtpLoginEntity.class)
    void shouldFailForInvalidOtpToken() throws JsonProcessingException {
        // given
        var request = ValidateOtpLoginViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("gandalf@shire.com") // hardcoded: matches stub email
                .withOtpCode("invalid") // hardcoded: code guaranteed not to match any DB record
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/otp-login-tokens")
        .then()
                .statusCode(401);
    }

    @Test
    void shouldFailWhenEmailIsMissing() throws JsonProcessingException {
        // given
        var request = ValidateOtpLoginViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail(null) // hardcoded: triggers @NotBlank on email
                .withOtpCode("123456") // hardcoded: arbitrary, isolate failure to email
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/otp-login-tokens")
        .then()
                .statusCode(400);
    }
}
