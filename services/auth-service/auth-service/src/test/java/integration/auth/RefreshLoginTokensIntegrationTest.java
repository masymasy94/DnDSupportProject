package integration.auth;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.view.model.vm.RefreshTokenViewModel;
import com.dndplatform.auth.view.model.vm.RefreshTokenViewModelBuilder;
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
class RefreshLoginTokensIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private RefreshTokenViewModel payloadTemplate;

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldFailForInvalidRefreshToken() throws JsonProcessingException {
        // given
        var request = RefreshTokenViewModelBuilder.toBuilder(payloadTemplate)
                .withToken("invalid-token") // hardcoded: token guaranteed not to exist in DB
                .withUserId(1L) // hardcoded: arbitrary user id
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/login-tokens/refreshed")
        .then()
                .statusCode(401);
    }

    @Test
    void shouldFailWhenRefreshTokenIsMissing() throws JsonProcessingException {
        // given
        var request = RefreshTokenViewModelBuilder.toBuilder(payloadTemplate)
                .withToken(null) // hardcoded: triggers @NotBlank
                .withUserId(1L) // hardcoded: arbitrary, isolate failure to token
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/auth/login-tokens/refreshed")
        .then()
                .statusCode(401);
    }
}
