package integration.compendium;

import com.dndplatform.compendium.domain.model.WeaponType;
import static org.mockito.ArgumentMatchers.any;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class WeaponTypeFindAllIntegrationTest {

    @InjectMock
    WeaponTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllWeaponTypes() {
        given(repository.findAllWeaponTypes(any())).willReturn(List.of(new WeaponType((short) 1, "Longsword", "Martial")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/weapon-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Longsword"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoWeaponTypes() {
        given(repository.findAllWeaponTypes(any())).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/weapon-types")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/weapon-types")
        .then()
            .statusCode(401);
    }
}
