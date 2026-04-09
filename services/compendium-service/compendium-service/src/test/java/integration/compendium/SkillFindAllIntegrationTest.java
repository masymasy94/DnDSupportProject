package integration.compendium;

import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SkillFindAllIntegrationTest {

    @InjectMock
    SkillFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllSkills() {
        // given
        given(repository.findAllSkills()).willReturn(List.of(
                new Skill((short) 1, "Acrobatics", (short) 2, "Dexterity") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        io.restassured.RestAssured.given() // FQN: io.restassured.given collides with BDDMockito.given imported above
        .when()
                .get("/api/compendium/skills")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Acrobatics"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoSkills() {
        // given
        given(repository.findAllSkills()).willReturn(List.of());

        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/skills")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(0));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/skills")
        .then()
                .statusCode(401);
    }
}
