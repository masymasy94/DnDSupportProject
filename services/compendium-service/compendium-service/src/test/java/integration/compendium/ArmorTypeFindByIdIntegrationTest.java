package integration.compendium;

import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ArmorTypeFindByIdIntegrationTest {

    @InjectMock
    ArmorTypeFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnArmorTypeById() {
        given(repository.findById(anyInt())).willReturn(Optional.of(new ArmorType((short) 1, "Heavy")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/armor-types/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON).body("name", org.hamcrest.Matchers.equalTo("Heavy"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenArmorTypeNotFound() {
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/armor-types/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/armor-types/1")
        .then()
            .statusCode(401);
    }
}
