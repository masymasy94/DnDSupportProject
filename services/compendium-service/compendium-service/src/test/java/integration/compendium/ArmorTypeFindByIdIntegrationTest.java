package integration.compendium;

import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindByIdRepository;
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
class ArmorTypeFindByIdIntegrationTest {

    @InjectMock
    ArmorTypeFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnArmorTypeById() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.of(
                new ArmorType((short) 1, "Heavy") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/armor-types/{id}", 1) // hardcoded: matches mocked id
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("name", equalTo("Heavy"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenArmorTypeNotFound() {
        // given
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        // when / then
        given()
        .when()
                .get("/api/compendium/armor-types/{id}", 999_999) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/compendium/armor-types/{id}", 1) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
