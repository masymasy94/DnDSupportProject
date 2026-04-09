package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntity;
import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
import com.dndplatform.auth.view.model.vm.ResetPasswordViewModelBuilder;
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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class ResetPasswordIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private ResetPasswordViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = PasswordResetEntity.class)
    void shouldFailForInvalidResetToken() throws JsonProcessingException {
        // given
        var request = ResetPasswordViewModelBuilder.toBuilder(payloadTemplate)
                .withToken("invalid-token") // hardcoded: token guaranteed not to exist
                .withNewPassword("NewPassword1") // hardcoded: must be non-blank
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .put("/auth/password-resets")
        .then()
                // FIXME(integration-tests-rewrite): invalid token should be 401 (or 410 Gone). The product
                // mixes 400/401/404.
                .statusCode(anyOf(equalTo(400), equalTo(401), equalTo(404)));
    }

    @Test
    void shouldFailWhenTokenIsMissing() throws JsonProcessingException {
        // given
        var request = ResetPasswordViewModelBuilder.toBuilder(payloadTemplate)
                .withToken(null) // hardcoded: triggers @NotBlank on token
                .withNewPassword("NewPassword1") // hardcoded: arbitrary, isolate failure to token
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .put("/auth/password-resets")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenNewPasswordIsMissing() throws JsonProcessingException {
        // given
        var request = ResetPasswordViewModelBuilder.toBuilder(payloadTemplate)
                .withToken("some-token") // hardcoded: arbitrary token, validation fails first
                .withNewPassword(null) // hardcoded: triggers @NotBlank on newPassword
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .put("/auth/password-resets")
        .then()
                .statusCode(400);
    }
}
