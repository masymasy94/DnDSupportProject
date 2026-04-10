package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SpellFindByIdIntegrationTest {

    @InjectMock
    SpellFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnSpellById() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.of(
                new Spell(1, "Fireball", 3, "Evocation", "1 action", "150 ft", "V, S, M", "Bat guano", "Instant", false, false, "Burn them", null, SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/spells/{id}", 1) // hardcoded: matches mocked id
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("name", equalTo("Fireball"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenSpellNotFound() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        // when / then
        given()
        .when()
                .get("/api/compendium/spells/{id}", 999_999) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/compendium/spells/{id}", 1) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
