package integration.compendium;

import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SpellFindAllIntegrationTest {

    @InjectMock
    SpellFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedSpells() {
        // given
        given(repository.findAllSpells(any(SpellFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(
                        new Spell(1, "Magic Missile", 1, "Evocation", "1 action", "120 ft", "V, S", null, "Instant", false, false, "Auto-hit darts", null, SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
                ), 0, 20, 1L)); // hardcoded: page=0, size=20, total=1 — default pagination snapshot

        // when / then
        io.restassured.RestAssured.given() // FQN: io.restassured.given collides with BDDMockito.given imported above
        .when()
                .get("/api/compendium/spells")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoSpells() {
        // given
        given(repository.findAllSpells(any(SpellFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(), 0, 20, 0L)); // hardcoded: empty page snapshot

        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/spells")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/spells")
        .then()
                .statusCode(401);
    }
}
