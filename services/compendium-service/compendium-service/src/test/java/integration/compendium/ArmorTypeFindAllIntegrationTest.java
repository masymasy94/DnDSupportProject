package integration.compendium;

import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindAllRepository;
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
class ArmorTypeFindAllIntegrationTest {

    @InjectMock
    ArmorTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllArmorTypes() {
        // given
        given(repository.findAllArmorTypes()).willReturn(List.of(
                new ArmorType((short) 1, "Light") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        io.restassured.RestAssured.given() // FQN: io.restassured.given collides with BDDMockito.given imported above
        .when()
                .get("/api/compendium/armor-types")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Light"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoArmorTypes() {
        // given
        given(repository.findAllArmorTypes()).willReturn(List.of());

        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/armor-types")
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
                .get("/api/compendium/armor-types")
        .then()
                .statusCode(401);
    }
}
