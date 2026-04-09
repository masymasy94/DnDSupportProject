package integration.combat;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByCampaignRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
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
        // given
        var encounter = new Encounter(
                1L, // hardcoded: deterministic id
                10L, // hardcoded: matches the campaignId query param below
                1L, // hardcoded: matches @TestSecurity user
                "Test Encounter", // hardcoded: deterministic name
                "desc", // hardcoded: arbitrary description
                EncounterStatus.DRAFT,
                3, // hardcoded: arbitrary current round
                4, // hardcoded: arbitrary turn order
                null,
                List.of(),
                LocalDateTime.now(),
                LocalDateTime.now());
        given(repository.findByCampaign(anyLong())).willReturn(List.of(encounter));

        // when / then
        given()
                .queryParam("campaignId", 10) // hardcoded: matches mocked encounter
        .when()
                .get("/encounters")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoEncountersForCampaign() {
        // given
        given(repository.findByCampaign(anyLong())).willReturn(List.of());

        // when / then
        given()
                .queryParam("campaignId", 99_999) // hardcoded: campaign id with no encounters
        .when()
                .get("/encounters")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(0));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("campaignId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/encounters")
        .then()
                .statusCode(401);
    }
}
