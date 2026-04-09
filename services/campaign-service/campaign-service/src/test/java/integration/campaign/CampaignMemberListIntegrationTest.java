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
class CampaignMemberListIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldListCampaignMembers() {
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"List Members Test","description":"desc","maxPlayers":4}
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

        var members = given()
        .when()
            .get("/campaigns/" + campaign.id() + "/members")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("$", Map.class);

        assertThat(members).isNotEmpty();
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldReturnOnlyDmMemberForFreshCampaign() {
        // A freshly created campaign has only the DM as a member (auto-added)
        var campaign = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Fresh Campaign","description":"desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        var members = given()
        .when()
            .get("/campaigns/" + campaign.id() + "/members")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().body().jsonPath().getList("$", Map.class);

        assertThat(members).hasSize(1);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .get("/campaigns/1/members")
        .then()
            .statusCode(401);
    }
}
