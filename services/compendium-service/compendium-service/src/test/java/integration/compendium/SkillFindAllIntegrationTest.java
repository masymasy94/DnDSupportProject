package integration.compendium;

import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SkillFindAllIntegrationTest {

    @InjectMock
    SkillFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllSkills() {
        given(repository.findAllSkills()).willReturn(List.of(new Skill((short) 1, "Acrobatics", (short) 2, "Dexterity")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/skills")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Acrobatics"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoSkills() {
        given(repository.findAllSkills()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/skills")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/skills")
        .then()
            .statusCode(401);
    }
}
