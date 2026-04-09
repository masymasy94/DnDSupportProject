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
class CampaignQuestUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateQuest() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Quest Update","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        var questId = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Original","description":"desc","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/quests")
        .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("id");

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Updated Title","description":"Updated","status":"COMPLETED","priority":"SIDE"}
                """)
        .when()
            .put("/campaigns/" + campaign.id() + "/quests/" + questId)
        .then()
            .statusCode(200)
            .body("title", org.hamcrest.Matchers.equalTo("Updated Title"))
            .body("status", org.hamcrest.Matchers.equalTo("COMPLETED"))
            .body("priority", org.hamcrest.Matchers.equalTo("SIDE"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenUpdatingNonexistentQuest() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Title","description":"desc","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .put("/campaigns/1/quests/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"title":"Title","description":"desc","status":"ACTIVE","priority":"MAIN"}
                """)
        .when()
            .put("/campaigns/1/quests/1")
        .then()
            .statusCode(401);
    }
}
