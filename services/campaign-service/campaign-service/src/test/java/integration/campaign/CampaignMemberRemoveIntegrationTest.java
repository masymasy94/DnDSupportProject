package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.view.model.vm.AddMemberRequest;
import com.dndplatform.campaign.view.model.vm.AddMemberRequestBuilder;
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

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignMemberRemoveIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateCampaignRequest createTemplate;

    @InjectRandom
    private AddMemberRequest addMemberTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldRemoveMemberFromCampaign() throws JsonProcessingException {
        // given
        var createRequest = CreateCampaignRequestBuilder.toBuilder(createTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Remove Test") // hardcoded: deterministic name
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(4) // hardcoded: arbitrary in valid range
                .withImageUrl(null) // hardcoded: optional
                .build();
        var campaign = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(createRequest))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(201)
                .extract().as(CampaignViewModel.class);

        var addMemberRequest = AddMemberRequestBuilder.toBuilder(addMemberTemplate)
                .withRequesterId(1L) // hardcoded: matches @TestSecurity user (DM)
                .withUserId(2L) // hardcoded: target player id
                .withCharacterId(null) // hardcoded: optional
                .build();
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(addMemberRequest))
        .when()
                .post("/campaigns/{id}/members", campaign.id())
        .then()
                .statusCode(201);

        // when / then
        given()
                .queryParam("requesterId", 1) // hardcoded: matches @TestSecurity user (DM)
        .when()
                .delete("/campaigns/{cid}/members/{mid}", campaign.id(), 2L) // hardcoded mid: matches the added member
        .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenRemovingFromNonexistentCampaign() {
        // when / then
        given()
                .queryParam("requesterId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .delete("/campaigns/{cid}/members/{mid}", 999_999L, 2L) // hardcoded: cid outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("requesterId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .delete("/campaigns/{cid}/members/{mid}", 1L, 2L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
