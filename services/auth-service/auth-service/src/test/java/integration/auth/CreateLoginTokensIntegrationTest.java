package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModelBuilder;
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
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(UserServiceWireMockResource.class)
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CreateLoginTokensIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateLoginTokensViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldCreateLoginTokens() throws JsonProcessingException {
        // given
        var request = CreateLoginTokensViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("gandalf") // hardcoded: matches stub in UserServiceWireMockResource
                .withPassword("YouShallNotPass1") // hardcoded: must match the stubbed credentials check
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/login-tokens")
        .then()
                .statusCode(201)
                .contentType(JSON)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    void shouldFailWhenUsernameIsMissing() throws JsonProcessingException {
        // given
        var request = CreateLoginTokensViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(null) // hardcoded: triggers @NotBlank
                .withPassword("YouShallNotPass1") // hardcoded: arbitrary, isolate failure to username
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/login-tokens")
        .then()
                .statusCode(400);
    }
}
