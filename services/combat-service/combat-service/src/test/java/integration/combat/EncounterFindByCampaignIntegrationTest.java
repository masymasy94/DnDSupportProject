package integration.combat;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByCampaignRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class EncounterFindByCampaignIntegrationTest {

    @InjectMock
    EncounterFindByCampaignRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEncountersByCampaignId() {
        var encounter = new Encounter(1L, 10L, 1L, "Test Encounter", "desc",
            EncounterStatus.DRAFT, 3, 4, null, List.of(),
            LocalDateTime.now(), LocalDateTime.now());

        given(repository.findByCampaign(anyLong())).willReturn(List.of(encounter));

        io.restassured.RestAssured.given()
            .queryParam("campaignId", 10)
        .when()
            .get("/encounters")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoEncountersForCampaign() {
        given(repository.findByCampaign(anyLong())).willReturn(List.of());

        io.restassured.RestAssured.given()
            .queryParam("campaignId", 99999)
        .when()
            .get("/encounters")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
            .queryParam("campaignId", 1)
        .when()
            .get("/encounters")
        .then()
            .statusCode(401);
    }
}
