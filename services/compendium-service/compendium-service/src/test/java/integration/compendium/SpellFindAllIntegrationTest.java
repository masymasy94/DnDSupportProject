package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.SpellFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SpellFindAllIntegrationTest {

    @InjectMock
    SpellFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedSpells() {
        given(repository.findAllSpells(any(com.dndplatform.compendium.domain.filter.SpellFilterCriteria.class)))
            .willReturn(new PagedResult<>(List.of(new Spell(1, "Magic Missile", 1, "Evocation", "1 action", "120 ft", "V, S", null, "Instant", false, false, "Auto-hit darts", null, SourceType.OFFICIAL, null, null, true)), 0, 20, 1L));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/spells")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoSpells() {
        given(repository.findAllSpells(any(com.dndplatform.compendium.domain.filter.SpellFilterCriteria.class)))
            .willReturn(new PagedResult<>(List.of(), 0, 20, 0L));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/spells")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/spells")
        .then()
            .statusCode(401);
    }
}
