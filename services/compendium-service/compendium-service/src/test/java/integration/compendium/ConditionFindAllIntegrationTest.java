package integration.compendium;

import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class ConditionFindAllIntegrationTest {

    @InjectMock
    ConditionFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllConditions() {
        given(repository.findAllConditions()).willReturn(List.of(new Condition((short) 1, "Blinded")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/conditions")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Blinded"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoConditions() {
        given(repository.findAllConditions()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/conditions")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/conditions")
        .then()
            .statusCode(401);
    }
}
