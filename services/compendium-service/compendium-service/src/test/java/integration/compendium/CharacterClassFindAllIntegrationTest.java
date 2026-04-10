package integration.compendium;

import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.CharacterClassFindAllRepository;
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
class CharacterClassFindAllIntegrationTest {

    @InjectMock
    CharacterClassFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllCharacterClasss() {
        // given
        given(repository.findAllClasses()).willReturn(List.of(
                new CharacterClass(1, "Wizard", "1d6", "Magic user", SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/classes")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Wizard"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoCharacterClasss() {
        // given
        given(repository.findAllClasses()).willReturn(List.of());

        // when / then
        given()
        .when()
                .get("/api/compendium/classes")
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
                .get("/api/compendium/classes")
        .then()
                .statusCode(401);
    }
}
