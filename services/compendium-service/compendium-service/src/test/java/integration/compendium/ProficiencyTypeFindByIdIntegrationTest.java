package integration.compendium;

import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ProficiencyTypeFindByIdIntegrationTest {

    @InjectMock
    ProficiencyTypeFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnProficiencyTypeById() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.of(
                new ProficiencyType((short) 1, "Armor") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        io.restassured.RestAssured.given() // FQN: io.restassured.given collides with BDDMockito.given imported above
        .when()
                .get("/api/compendium/proficiency-types/{id}", 1) // hardcoded: matches mocked id
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("name", equalTo("Armor"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenProficiencyTypeNotFound() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/proficiency-types/{id}", 999_999) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/proficiency-types/{id}", 1) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
