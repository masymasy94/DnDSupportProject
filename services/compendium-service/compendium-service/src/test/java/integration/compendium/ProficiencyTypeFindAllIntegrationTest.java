package integration.compendium;

import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ProficiencyTypeFindAllIntegrationTest {

    @InjectMock
    ProficiencyTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllProficiencyTypes() {
        // given
        given(repository.findAllProficiencyTypes()).willReturn(List.of(
                new ProficiencyType((short) 1, "Weapons") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/proficiency-types")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Weapons"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoProficiencyTypes() {
        // given
        given(repository.findAllProficiencyTypes()).willReturn(List.of());

        // when / then
        given()
        .when()
                .get("/api/compendium/proficiency-types")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(0));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/compendium/proficiency-types")
        .then()
                .statusCode(401);
    }
}
