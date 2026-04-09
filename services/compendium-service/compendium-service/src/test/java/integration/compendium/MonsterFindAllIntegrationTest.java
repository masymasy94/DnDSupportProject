package integration.compendium;

import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.MonsterFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class MonsterFindAllIntegrationTest {

    @InjectMock
    MonsterFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedMonsters() {
        // given
        given(repository.findAllMonsters(any(MonsterFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(
                        new Monster(1, "goblin", "Goblin", "Small", "humanoid", null, "neutral evil", 15, "leather", 7, "2d6", "30 ft", 8, 14, 10, 10, 8, 8, null, null, null, "Common, Goblin", "1/4", 50, 2, null, null, null, null, null, null, null, "A small humanoid", SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
                ), 0, 20, 1L)); // hardcoded: page=0, size=20, total=1 — default pagination snapshot

        // when / then
        io.restassured.RestAssured.given() // FQN: io.restassured.given collides with BDDMockito.given imported above
        .when()
                .get("/api/compendium/monsters")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoMonsters() {
        // given
        given(repository.findAllMonsters(any(MonsterFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(), 0, 20, 0L)); // hardcoded: empty page snapshot

        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/monsters")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/monsters")
        .then()
                .statusCode(401);
    }
}
