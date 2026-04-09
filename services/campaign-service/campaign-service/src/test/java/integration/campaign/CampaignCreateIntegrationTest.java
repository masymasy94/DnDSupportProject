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
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CampaignCreateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldCreateCampaign() {
        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Curse of Strahd","description":"Gothic horror","maxPlayers":6}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(CampaignViewModel.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Curse of Strahd");
        assertThat(response.description()).isEqualTo("Gothic horror");
        assertThat(response.dungeonMasterId()).isEqualTo(1L);
        assertThat(response.maxPlayers()).isEqualTo(6);
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenNameIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"","description":"desc","maxPlayers":6}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(400);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenMaxPlayersExceeds20() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Campaign","description":"desc","maxPlayers":25}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Campaign","description":"desc","maxPlayers":6}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(401);
    }
}
