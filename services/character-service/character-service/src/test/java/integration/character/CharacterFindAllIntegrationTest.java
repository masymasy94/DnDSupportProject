package integration.character;

import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.domain.repository.CharacterFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class CharacterFindAllIntegrationTest {

    @InjectMock
    CharacterFindAllRepository repository;

    @Test
    void shouldReturnPagedCharacters() {
        // given
        given(repository.findAll(anyInt(), anyInt()))
                .willReturn(new PagedResult(List.of(), 0, 10, 0L, 0)); // hardcoded: empty page, isolates the response shape assertions

        // when / then
        given()
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/characters")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("page", equalTo(0))
                .body("size", equalTo(10))
                .body("totalElements", equalTo(0));
    }
}
