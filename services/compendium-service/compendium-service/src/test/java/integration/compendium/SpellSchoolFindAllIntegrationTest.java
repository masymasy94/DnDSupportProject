package integration.compendium;

import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SpellSchoolFindAllIntegrationTest {

    @InjectMock
    SpellSchoolFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllSpellSchools() {
        given(repository.findAllSpellSchools()).willReturn(List.of(new SpellSchool((short) 1, "Evocation")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/spell-schools")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Evocation"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoSpellSchools() {
        given(repository.findAllSpellSchools()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/spell-schools")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/spell-schools")
        .then()
            .statusCode(401);
    }
}
