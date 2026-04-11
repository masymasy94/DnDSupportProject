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

import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
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

        // then — verify user-service was called with correct credentials
        UserServiceWireMockResource.getServer().verify(
                postRequestedFor(urlPathMatching("/users/credentials-validation/?"))
                        .withRequestBody(matchingJsonPath("$.username", equalTo("gandalf"))));
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
                .statusCode(400)
                .contentType(JSON);
    }

    @Test
    @DeleteEntities(from = RefreshTokenEntity.class)
    void shouldFailWhenUserServiceReturnsServerError() throws JsonProcessingException {
        // given — add a 500-response stub that takes precedence over the default
        // happy-path stub (most recently added wins at same priority). Keep the
        // reference so we can remove ONLY this stub in the cleanup phase.
        var server = UserServiceWireMockResource.getServer();
        StubMapping errorStub = server.stubFor(
                post(urlPathMatching("/users/credentials-validation/?"))
                        .willReturn(aResponse().withStatus(500)));

        try {
            var request = CreateLoginTokensViewModelBuilder.toBuilder(payloadTemplate)
                    .withUsername("gandalf") // hardcoded: matches happy-path stub, overridden here to 500
                    .withPassword("YouShallNotPass1") // hardcoded: arbitrary, credentials check is mocked
                    .build();

            // when / then
            given()
                    .contentType(JSON)
                    .body(objectMapper.writeValueAsString(request))
            .when()
                    .post("/auth/login-tokens")
            .then()
                    .statusCode(500);
        } finally {
            // cleanup: remove only the 500 stub so the happy-path stub from
            // UserServiceWireMockResource.start() takes over again for subsequent tests.
            server.removeStubMapping(errorStub);
        }
    }
}
