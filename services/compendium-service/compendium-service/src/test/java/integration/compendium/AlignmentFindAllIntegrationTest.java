package integration.compendium;

import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindAllRepository;
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
class AlignmentFindAllIntegrationTest {

    @InjectMock
    AlignmentFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllAlignments() {
        // given
        given(repository.findAllAlignment()).willReturn(List.of(
                new Alignment((short) 1, "LG", "Lawful Good"), // hardcoded: deterministic seed for assertion
                new Alignment((short) 2, "CN", "Chaotic Neutral") // hardcoded: deterministic seed for assertion
        ));

        // when / then
        given()
        .when()
                .get("/api/compendium/alignments")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(2))
                .body("name", hasItem("Lawful Good"))
                .body("name", hasItem("Chaotic Neutral"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoAlignments() {
        // given
        given(repository.findAllAlignment()).willReturn(List.of());

        // when / then
        given()
        .when()
                .get("/api/compendium/alignments")
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
                .get("/api/compendium/alignments")
        .then()
                .statusCode(401);
    }
}
