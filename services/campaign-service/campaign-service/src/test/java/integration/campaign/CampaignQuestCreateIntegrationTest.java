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
class CampaignQuestCreateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldCreateQuest() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Quest Create","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Find the Artifact","description":"Locate the lost item","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/quests")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("title", org.hamcrest.Matchers.equalTo("Find the Artifact"))
            .body("status", org.hamcrest.Matchers.equalTo("ACTIVE"))
            .body("priority", org.hamcrest.Matchers.equalTo("MAIN"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenTitleIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"","description":"desc","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .post("/campaigns/1/quests")
        .then()
            .statusCode(400);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenCampaignNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Quest","description":"desc","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .post("/campaigns/999999/quests")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Quest","description":"desc","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .post("/campaigns/1/quests")
        .then()
            .statusCode(401);
    }
}
