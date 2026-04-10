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

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignDeleteIntegrationTest {

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
    void shouldDeleteCampaign() throws JsonProcessingException {
        // given
        var createRequest = CreateCampaignRequestBuilder.toBuilder(createTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Delete Me") // hardcoded: deterministic name
                .withDescription("desc") // hardcoded: arbitrary
                .withMaxPlayers(4) // hardcoded: arbitrary in valid range
                .withImageUrl(null) // hardcoded: optional, omit
                .build();
        var created = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(createRequest))
        .when()
                .post("/campaigns")
        .then()
                .statusCode(201)
                .extract().as(CampaignViewModel.class);

        // when
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .delete("/campaigns/{id}", created.id())
        .then()
                .statusCode(204);

        // then
        given()
        .when()
                .get("/campaigns/{id}", created.id())
        .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenDeletingNonexistentCampaign() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .delete("/campaigns/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }
}
