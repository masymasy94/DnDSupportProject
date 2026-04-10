package integration.compendium;

import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.SpeciesFindAllRepository;
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
class SpeciesFindAllIntegrationTest {

    @InjectMock
    SpeciesFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllSpeciess() {
        // given
        given(repository.findAllSpecies()).willReturn(List.of(
                new Species(1, "Elf", "Long-lived", SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/species")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Elf"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoSpeciess() {
        // given
        given(repository.findAllSpecies()).willReturn(List.of());

        // when / then
        given()
        .when()
                .get("/api/compendium/species")
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
                .get("/api/compendium/species")
        .then()
                .statusCode(401);
    }
}
