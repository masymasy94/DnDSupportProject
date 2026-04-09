package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequestBuilder;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequestBuilder;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequestBuilder;
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
class CampaignNoteUpdateIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateCampaignRequest createCampaignTemplate;

    @InjectRandom
    private CreateNoteRequest createNoteTemplate;

    @InjectRandom
    private UpdateNoteRequest updateNoteTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateNote() throws JsonProcessingException {
        // given
        var campaign = createCampaign("Note Update");
        var createNote = CreateNoteRequestBuilder.toBuilder(createNoteTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withTitle("Original") // hardcoded: deterministic original title
                .withContent("content") // hardcoded: arbitrary
                .withVisibility("PUBLIC") // hardcoded: arbitrary
                .build();
        var noteId = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(createNote))
        .when()
                .post("/campaigns/{id}/notes", campaign.id())
        .then()
                .statusCode(200) // FIXME(integration-tests-rewrite): POST creation should return 201
                .extract().body().jsonPath().getLong("id");

        var updateRequest = UpdateNoteRequestBuilder.toBuilder(updateNoteTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withTitle("Updated Title") // hardcoded: deterministic updated title
                .withContent("Updated content") // hardcoded: deterministic updated content
                .withVisibility("PRIVATE") // hardcoded: enum-like product field
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(updateRequest))
        .when()
                .put("/campaigns/{cid}/notes/{nid}", campaign.id(), noteId)
        .then()
                .statusCode(200)
                .body("title", equalTo("Updated Title"))
                .body("content", equalTo("Updated content"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenUpdatingNonexistentNote() throws JsonProcessingException {
        // given
        var request = UpdateNoteRequestBuilder.toBuilder(updateNoteTemplate)
                .withUserId(1L) // hardcoded: matches @TestSecurity user
                .withTitle("Title") // hardcoded: arbitrary, isolate failure to missing note
                .withContent("content") // hardcoded: arbitrary
                .withVisibility("PUBLIC") // hardcoded: arbitrary
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .put("/campaigns/{cid}/notes/{nid}", 1L, 999_999L) // hardcoded nid: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() throws JsonProcessingException {
        // given
        var request = UpdateNoteRequestBuilder.toBuilder(updateNoteTemplate)
                .withUserId(1L) // hardcoded: arbitrary, auth fails first
                .withTitle("Title") // hardcoded: arbitrary, auth fails first
                .withContent("content") // hardcoded: arbitrary
                .withVisibility("PUBLIC") // hardcoded: arbitrary
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .put("/campaigns/{cid}/notes/{nid}", 1L, 1L) // hardcoded: arbitrary, auth fails first
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
