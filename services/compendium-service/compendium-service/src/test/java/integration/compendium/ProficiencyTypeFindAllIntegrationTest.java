package integration.compendium;

import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ProficiencyTypeFindAllIntegrationTest {

    @InjectMock
    ProficiencyTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllProficiencyTypes() {
        given(repository.findAllProficiencyTypes()).willReturn(List.of(new ProficiencyType((short) 1, "Weapons")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/proficiency-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Weapons"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoProficiencyTypes() {
        given(repository.findAllProficiencyTypes()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/proficiency-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/proficiency-types")
        .then()
            .statusCode(401);
    }
}
