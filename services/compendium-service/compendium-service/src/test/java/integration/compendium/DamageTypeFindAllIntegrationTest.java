package integration.compendium;

import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class DamageTypeFindAllIntegrationTest {

    @InjectMock
    DamageTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllDamageTypes() {
        given(repository.findAllDamageTypes()).willReturn(List.of(new DamageType((short) 1, "Fire")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/damage-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Fire"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoDamageTypes() {
        given(repository.findAllDamageTypes()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/damage-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/damage-types")
        .then()
            .statusCode(401);
    }
}
