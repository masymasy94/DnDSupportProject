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
class CampaignMemberAddIntegrationTest {

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
    void shouldAddMemberToCampaign() throws JsonProcessingException {
        // given
        var createRequest = CreateCampaignRequestBuilder.toBuilder(createTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Member Add Test") // hardcoded: deterministic name
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
                .withRequesterId(1L) // hardcoded: matches @TestSecurity user (the DM)
                .withUserId(2L) // hardcoded: arbitrary new player id
                .withCharacterId(null) // hardcoded: optional, omit
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(addMemberRequest))
        .when()
                .post("/campaigns/{id}/members", campaign.id())
        .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenAddingMemberToNonexistentCampaign() throws JsonProcessingException {
        // given
        var request = AddMemberRequestBuilder.toBuilder(addMemberTemplate)
                .withRequesterId(1L) // hardcoded: matches @TestSecurity user
                .withUserId(2L) // hardcoded: arbitrary
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns/{id}/members", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenRequesterIdIsMissing() throws JsonProcessingException {
        // given
        var request = AddMemberRequestBuilder.toBuilder(addMemberTemplate)
                .withRequesterId(null) // hardcoded: triggers @NotNull on requesterId
                .withUserId(2L) // hardcoded: arbitrary, isolate failure
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns/{id}/members", 1L) // hardcoded: arbitrary, validation fails first
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenNotAuthenticated() throws JsonProcessingException {
        // given
        var request = AddMemberRequestBuilder.toBuilder(addMemberTemplate)
                .withRequesterId(1L) // hardcoded: arbitrary, auth fails first
                .withUserId(2L) // hardcoded: arbitrary, auth fails first
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/campaigns/{id}/members", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
