package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
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
class CampaignFindAllIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateCampaignRequest createTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldReturnPagedCampaignsForUser() throws JsonProcessingException {
        // given
        var createRequest = CreateCampaignRequestBuilder.toBuilder(createTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("List Test") // hardcoded: deterministic name
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(4) // hardcoded: arbitrary in valid range
                .withImageUrl(null) // hardcoded: optional
                .build();
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(createRequest))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(201);

        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/campaigns")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPageWhenUserHasNoCampaigns() {
        // when / then
        given()
                .queryParam("userId", 99_999L) // hardcoded: user id with no campaigns
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/campaigns")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/campaigns")
        .then()
                .statusCode(401);
    }
}
