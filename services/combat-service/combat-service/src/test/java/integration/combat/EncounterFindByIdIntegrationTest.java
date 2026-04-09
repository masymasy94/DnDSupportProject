package integration.combat;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        var encounter = new Encounter(1L, 10L, 1L, "Goblin Ambush", "A roadside ambush",
            EncounterStatus.DRAFT, 3, 4, null, List.of(),
            LocalDateTime.now(), LocalDateTime.now());

        given(repository.findById(anyLong())).willReturn(Optional.of(encounter));

        io.restassured.RestAssured.given()
        .when()
            .get("/encounters/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", equalTo("Goblin Ambush"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenEncounterNotFound() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        io.restassured.RestAssured.given()
        .when()
            .get("/encounters/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/encounters/1")
        .then()
            .statusCode(401);
    }
}
