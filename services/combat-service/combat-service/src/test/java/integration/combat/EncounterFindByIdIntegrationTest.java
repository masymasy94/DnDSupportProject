package integration.combat;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class EncounterFindByIdIntegrationTest {

    @InjectMock
    EncounterFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFindEncounterById() {
        // given
        var encounter = new Encounter(
                1L, // hardcoded: deterministic id for assertions
                10L, // hardcoded: arbitrary campaign id
                1L, // hardcoded: matches @TestSecurity user
                "Goblin Ambush", // hardcoded: deterministic name for assertions
                "A roadside ambush", // hardcoded: deterministic description
                EncounterStatus.DRAFT,
                3, // hardcoded: arbitrary current round
                4, // hardcoded: arbitrary turn order
                null,
                List.of(),
                LocalDateTime.now(),
                LocalDateTime.now());
        given(repository.findById(anyLong())).willReturn(Optional.of(encounter));

        // when / then
        given()
        .when()
                .get("/encounters/{id}", 1L) // hardcoded: matches mocked encounter id
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("name", equalTo("Goblin Ambush"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenEncounterNotFound() {
        // given
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        given()
        .when()
                .get("/encounters/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/encounters/{id}", 1L) // hardcoded: arbitrary id, auth fails first
        .then()
                .statusCode(401);
    }
}
