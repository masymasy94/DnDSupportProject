package integration.compendium;

import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ProficiencyTypeFindByIdIntegrationTest {

    @InjectMock
    ProficiencyTypeFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnProficiencyTypeById() {
        given(repository.findById(anyInt())).willReturn(Optional.of(new ProficiencyType((short) 1, "Armor")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/proficiency-types/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON).body("name", org.hamcrest.Matchers.equalTo("Armor"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenProficiencyTypeNotFound() {
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/proficiency-types/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/proficiency-types/1")
        .then()
            .statusCode(401);
    }
}
