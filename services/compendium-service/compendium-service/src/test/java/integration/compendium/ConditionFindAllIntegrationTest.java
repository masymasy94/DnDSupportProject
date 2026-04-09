package integration.compendium;

import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindAllRepository;
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
class ConditionFindAllIntegrationTest {

    @InjectMock
    ConditionFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllConditions() {
        // given
        given(repository.findAllConditions()).willReturn(List.of(
                new Condition((short) 1, "Blinded") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        io.restassured.RestAssured.given() // FQN: io.restassured.given collides with BDDMockito.given imported above
        .when()
                .get("/api/compendium/conditions")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Blinded"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoConditions() {
        // given
        given(repository.findAllConditions()).willReturn(List.of());

        // when / then
        io.restassured.RestAssured.given() // FQN: collides with BDDMockito.given
        .when()
                .get("/api/compendium/conditions")
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
                .get("/api/compendium/conditions")
        .then()
                .statusCode(401);
    }
}
