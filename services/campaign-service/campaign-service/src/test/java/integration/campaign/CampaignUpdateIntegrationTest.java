package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequestBuilder;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequestBuilder;
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
class CampaignUpdateIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateCampaignRequest createTemplate;

    @InjectRandom
    private UpdateCampaignRequest updateTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateCampaign() throws JsonProcessingException {
        // given
        var createRequest = CreateCampaignRequestBuilder.toBuilder(createTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Original") // hardcoded: deterministic original name
                .withDescription("Original desc") // hardcoded: deterministic
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

        var updateRequest = UpdateCampaignRequestBuilder.toBuilder(updateTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Updated Name") // hardcoded: deterministic new name
                .withDescription("Updated desc") // hardcoded: deterministic
                .withStatus("ACTIVE") // hardcoded: enum-like product field
                .withMaxPlayers(6) // hardcoded: arbitrary in valid range
                .withImageUrl(null) // hardcoded: optional, omit
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(updateRequest))
        .when()
                .put("/campaigns/{id}", created.id())
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(CampaignViewModel.class);

        // then
        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.description()).isEqualTo("Updated desc");
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.maxPlayers()).isEqualTo(6);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenUpdatingNonexistentCampaign() throws JsonProcessingException {
        // given
        var request = UpdateCampaignRequestBuilder.toBuilder(updateTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withName("Name") // hardcoded: arbitrary, isolate failure to missing campaign
                .withDescription("desc") // hardcoded: arbitrary
                .withStatus("ACTIVE") // hardcoded: arbitrary
                .withMaxPlayers(4) // hardcoded: arbitrary in valid range
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .put("/campaigns/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }
}
