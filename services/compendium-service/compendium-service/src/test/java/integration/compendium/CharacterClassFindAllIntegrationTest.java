package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class CharacterClassFindAllIntegrationTest {

    @InjectMock
    CharacterClassFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllCharacterClasss() {
        given(repository.findAllClasses()).willReturn(List.of(new CharacterClass(1, "Wizard", "1d6", "Magic user", SourceType.OFFICIAL, null, null, true)));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/classes")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Wizard"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoCharacterClasss() {
        given(repository.findAllClasses()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/classes")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/classes")
        .then()
            .statusCode(401);
    }
}
