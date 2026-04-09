package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SpeciesFindAllIntegrationTest {

    @InjectMock
    SpeciesFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllSpeciess() {
        given(repository.findAllSpecies()).willReturn(List.of(new Species(1, "Elf", "Long-lived", SourceType.OFFICIAL, null, null, true)));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/species")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Elf"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoSpeciess() {
        given(repository.findAllSpecies()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/species")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/species")
        .then()
            .statusCode(401);
    }
}
