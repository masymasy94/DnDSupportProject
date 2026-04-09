package integration.campaign;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignNoteListIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldListNotes() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Note List","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Note A","content":"content","visibility":"PUBLIC"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(200);

        var notes = given()
            .queryParam("userId", 1)
        .when()
            .get("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("$", Map.class);

        assertThat(notes).hasSize(1);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldReturnEmptyListWhenNoNotes() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Empty Notes","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        var notes = given()
            .queryParam("userId", 1)
        .when()
            .get("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("$", Map.class);

        assertThat(notes).isEmpty();
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("userId", 1)
        .when()
            .get("/campaigns/1/notes")
        .then()
            .statusCode(401);
    }
}
