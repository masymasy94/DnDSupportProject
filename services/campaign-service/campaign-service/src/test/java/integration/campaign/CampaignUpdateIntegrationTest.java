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
class CampaignUpdateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = CampaignMemberEntity.class)
    @DeleteEntities(from = CampaignNoteEntity.class)
    @DeleteEntities(from = CampaignQuestEntity.class)
    @DeleteEntities(from = CampaignEntity.class)
    void shouldUpdateCampaign() {
        var created = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Original","description":"Original desc","maxPlayers":4}
                """)
        .when()
            .post("/campaigns")
        .then()
            .statusCode(200)
            .extract().as(CampaignViewModel.class);

        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Updated Name","description":"Updated desc","status":"ACTIVE","maxPlayers":6}
                """)
        .when()
            .put("/campaigns/" + created.id())
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(CampaignViewModel.class);

        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.description()).isEqualTo("Updated desc");
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.maxPlayers()).isEqualTo(6);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenUpdatingNonexistentCampaign() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"userId":1,"name":"Name","description":"desc","status":"ACTIVE","maxPlayers":4}
                """)
        .when()
            .put("/campaigns/999999")
        .then()
            .statusCode(404);
    }
}
