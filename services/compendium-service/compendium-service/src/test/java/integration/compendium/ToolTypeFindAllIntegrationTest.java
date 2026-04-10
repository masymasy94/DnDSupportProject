package integration.compendium;

import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindAllRepository;
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
class ToolTypeFindAllIntegrationTest {

    @InjectMock
    ToolTypeFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllToolTypes() {
        // given
        given(repository.findAllToolTypes(any())).willReturn(List.of(
                new ToolType((short) 1, "Smith Tools", "Artisan") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/tool-types")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("name", hasItem("Smith Tools"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoToolTypes() {
        // given
        given(repository.findAllToolTypes(any())).willReturn(List.of());

        // when / then
        given()
        .when()
                .get("/api/compendium/tool-types")
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
                .get("/api/compendium/tool-types")
        .then()
                .statusCode(401);
    }
}
