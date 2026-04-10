package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntity;
import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModelBuilder;
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
class RequestPasswordResetIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private RequestPasswordResetViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = PasswordResetEntity.class)
    void shouldRequestPasswordReset() throws JsonProcessingException {
        // given
        var request = RequestPasswordResetViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("gandalf@shire.com") // hardcoded: matches the email-lookup stub in UserServiceWireMockResource
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/password-resets")
        .then()
                .statusCode(202);
    }

    @Test
    void shouldFailWhenEmailIsInvalid() throws JsonProcessingException {
        // given
        var request = RequestPasswordResetViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("not-an-email") // hardcoded: triggers @Email validation
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/password-resets")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenEmailIsBlank() throws JsonProcessingException {
        // given
        var request = RequestPasswordResetViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("") // hardcoded: triggers @NotBlank
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/password-resets")
        .then()
                .statusCode(400);
    }
}
