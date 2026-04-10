package integration.compendium;

import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.SpeciesFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class SpeciesFindByIdIntegrationTest {

    @InjectMock
    SpeciesFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnSpeciesById() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.of(
                new Species(1, "Dwarf", "Stout", SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/species/{id}", 1) // hardcoded: matches mocked id
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("name", equalTo("Dwarf"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenSpeciesNotFound() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        // when / then
        given()
        .when()
                .get("/api/compendium/species/{id}", 999_999) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/compendium/species/{id}", 1) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
