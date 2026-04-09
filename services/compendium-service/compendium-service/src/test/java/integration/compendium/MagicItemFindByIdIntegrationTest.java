package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.repository.MagicItemFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class MagicItemFindByIdIntegrationTest {

    @InjectMock
    MagicItemFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnMagicItemById() {
        given(repository.findById(anyInt())).willReturn(Optional.of(new MagicItem(1, "Cloak of Invisibility", "Legendary", "Wondrous Item", true, null, "Become invisible", null, SourceType.OFFICIAL, null, null, true)));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/magic-items/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON).body("name", org.hamcrest.Matchers.equalTo("Cloak of Invisibility"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenMagicItemNotFound() {
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/magic-items/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/magic-items/1")
        .then()
            .statusCode(401);
    }
}
