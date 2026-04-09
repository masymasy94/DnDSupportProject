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
class CampaignMemberRemoveIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldRemoveMemberFromCampaign() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Remove Test","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"requesterId":1,"userId":2}
                """)
        .when()
            .post("/campaigns/" + campaign.id() + "/members")
        .then()
            .statusCode(200);

        given()
            .queryParam("requesterId", 1)
        .when()
            .delete("/campaigns/" + campaign.id() + "/members/2")
        .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenRemovingFromNonexistentCampaign() {
        given()
            .queryParam("requesterId", 1)
        .when()
            .delete("/campaigns/999999/members/2")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("requesterId", 1)
        .when()
            .delete("/campaigns/1/members/2")
        .then()
            .statusCode(401);
    }
}
