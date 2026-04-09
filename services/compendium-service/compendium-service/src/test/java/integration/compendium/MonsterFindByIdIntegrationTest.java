package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.repository.MonsterFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class MonsterFindByIdIntegrationTest {

    @InjectMock
    MonsterFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnMonsterById() {
        given(repository.findById(anyInt())).willReturn(Optional.of(new Monster(1, "ogre", "Ogre", "Large", "giant", null, "chaotic evil", 11, "hide", 59, "7d10+21", "40 ft", 19, 8, 16, 5, 7, 7, null, null, null, "Common, Giant", "2", 450, 2, null, null, null, null, null, null, null, "A hulking giant", SourceType.OFFICIAL, null, null, true)));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/monsters/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON).body("name", org.hamcrest.Matchers.equalTo("Ogre"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenMonsterNotFound() {
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/monsters/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/monsters/1")
        .then()
            .statusCode(401);
    }
}
