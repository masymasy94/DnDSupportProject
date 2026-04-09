package integration.compendium;

import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ArmorTypeFindAllIntegrationTest {

    @InjectMock
    ArmorTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllArmorTypes() {
        given(repository.findAllArmorTypes()).willReturn(List.of(new ArmorType((short) 1, "Light")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/armor-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Light"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoArmorTypes() {
        given(repository.findAllArmorTypes()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/armor-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/armor-types")
        .then()
            .statusCode(401);
    }
}
