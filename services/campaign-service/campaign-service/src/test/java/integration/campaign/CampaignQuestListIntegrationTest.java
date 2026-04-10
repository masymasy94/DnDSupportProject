package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequestBuilder;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequestBuilder;
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

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignQuestListIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateCampaignRequest createCampaignTemplate;

    @InjectRandom
    private CreateQuestRequest createQuestTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldListQuests() throws JsonProcessingException {
        // given
        var campaign = createCampaign("Quest List");
        var questRequest = CreateQuestRequestBuilder.toBuilder(createQuestTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withTitle("Quest A") // hardcoded: deterministic title
                .withDescription("desc") // hardcoded: arbitrary
                .withStatus("ACTIVE") // hardcoded: arbitrary
                .withPriority("MAIN") // hardcoded: arbitrary
                .build();
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(questRequest))
        .when()
                .post("/campaigns/{id}/quests", campaign.id())
        .then()
                .statusCode(201);

        // when
        var quests = given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/campaigns/{id}/quests", campaign.id())
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().body().jsonPath().getList("$", Map.class);

        // then
        assertThat(quests).hasSize(1);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldReturnEmptyListWhenNoQuests() throws JsonProcessingException {
        // given
        var campaign = createCampaign("Empty Quests");

        // when
        var quests = given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/campaigns/{id}/quests", campaign.id())
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().body().jsonPath().getList("$", Map.class);

        // then
        assertThat(quests).isEmpty();
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/campaigns/{id}/quests", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }

    private CampaignViewModel createCampaign(String name) throws JsonProcessingException {
        var request = CreateCampaignRequestBuilder.toBuilder(createCampaignTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName(name)
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(4) // hardcoded: arbitrary in valid range
                .withImageUrl(null) // hardcoded: optional
                .build();
        return given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(201)
                .extract().as(CampaignViewModel.class);
    }
}
