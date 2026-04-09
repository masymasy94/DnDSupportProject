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

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignQuestFindByIdIntegrationTest {

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
    void shouldFindQuestById() throws JsonProcessingException {
        // given
        var campaign = createCampaign("Quest Find");
        var questRequest = CreateQuestRequestBuilder.toBuilder(createQuestTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withTitle("Find Me") // hardcoded: deterministic title for assertion
                .withDescription("desc") // hardcoded: arbitrary
                .withStatus("ACTIVE") // hardcoded: arbitrary
                .withPriority("MAIN") // hardcoded: arbitrary
                .build();
        var questId = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(questRequest))
        .when()
                .post("/campaigns/{id}/quests", campaign.id())
        .then()
                .statusCode(200) // FIXME(integration-tests-rewrite): POST creation should return 201
                .extract().body().jsonPath().getLong("id");

        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/campaigns/{cid}/quests/{qid}", campaign.id(), questId)
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("id", equalTo((int) questId))
                .body("title", equalTo("Find Me"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldFailWhenQuestNotFound() throws JsonProcessingException {
        // given
        var campaign = createCampaign("Empty");

        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/campaigns/{cid}/quests/{qid}", campaign.id(), 999_999L) // hardcoded qid: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/campaigns/{cid}/quests/{qid}", 1L, 1L) // hardcoded: arbitrary, auth fails first
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
                .statusCode(200) // FIXME(integration-tests-rewrite): POST creation should return 201
                .extract().as(CampaignViewModel.class);
    }
}
