package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequestBuilder;
import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignCreateIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateCampaignRequest payloadTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldCreateCampaign() throws JsonProcessingException {
        // given
        var request = CreateCampaignRequestBuilder.toBuilder(payloadTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Curse of Strahd") // hardcoded: deterministic name for assertions
                .withDescription("Gothic horror") // hardcoded: deterministic description
                .withMaxPlayers(6) // hardcoded: arbitrary in [1..20]
                .withImageUrl(null) // hardcoded: optional, omit to keep payload minimal
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(201)
                .contentType(JSON)
                .extract().as(CampaignViewModel.class);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Curse of Strahd");
        assertThat(response.description()).isEqualTo("Gothic horror");
        assertThat(response.dungeonMasterId()).isEqualTo(1L);
        assertThat(response.maxPlayers()).isEqualTo(6);
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenNameIsBlank() throws JsonProcessingException {
        // given
        var request = CreateCampaignRequestBuilder.toBuilder(payloadTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("") // hardcoded: triggers @NotBlank
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(6) // hardcoded: in valid range, isolate failure to name
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenMaxPlayersExceedsLimit() throws JsonProcessingException {
        // given
        var request = CreateCampaignRequestBuilder.toBuilder(payloadTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Campaign") // hardcoded: valid name, isolate failure to maxPlayers
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(25) // hardcoded: above @Max(20) constraint
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenNotAuthenticated() throws JsonProcessingException {
        // given
        var request = CreateCampaignRequestBuilder.toBuilder(payloadTemplate)
                .withUserId(1L) // hardcoded: arbitrary, auth fails first
                .withName("Campaign") // hardcoded: arbitrary, auth fails first
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(6) // hardcoded: arbitrary in valid range
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(401);
    }
}
