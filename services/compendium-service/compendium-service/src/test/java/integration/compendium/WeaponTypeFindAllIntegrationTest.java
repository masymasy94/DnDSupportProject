package integration.compendium;

import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class WeaponTypeFindAllIntegrationTest {

    @InjectMock
    WeaponTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllWeaponTypes() {
        // given
        given(repository.findAllWeaponTypes(any())).willReturn(List.of(
                new WeaponType((short) 1, "Longsword", "Martial") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/weapon-types")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Longsword"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoWeaponTypes() {
        // given
        given(repository.findAllWeaponTypes(any())).willReturn(List.of());

        // when / then
        given()
        .when()
                .get("/api/compendium/weapon-types")
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
                .get("/api/compendium/weapon-types")
        .then()
                .statusCode(401);
    }
}
