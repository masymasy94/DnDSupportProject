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

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignNoteUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateNote() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Note Update","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        var noteId = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Original","content":"content","visibility":"PUBLIC"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/notes")
        .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("id");

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Updated Title","content":"Updated content","visibility":"PRIVATE"}
                """)
        .when()
            .put("/campaigns/" + campaign.id() + "/notes/" + noteId)
        .then()
            .statusCode(200)
            .body("title", org.hamcrest.Matchers.equalTo("Updated Title"))
            .body("content", org.hamcrest.Matchers.equalTo("Updated content"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenUpdatingNonexistentNote() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Title","content":"content","visibility":"PUBLIC"}
                """)
        .when()
            .put("/campaigns/1/notes/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Title","content":"content","visibility":"PUBLIC"}
                """)
        .when()
            .put("/campaigns/1/notes/1")
        .then()
            .statusCode(401);
    }
}
