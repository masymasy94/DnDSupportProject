package integration.character;

import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.domain.repository.CharacterFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class CharacterFindAllIntegrationTest {

    @InjectMock
    CharacterFindAllRepository repository;

    @Test
    void shouldReturnPagedCharacters() {
        given(repository.findAll(anyInt(), anyInt()))
            .willReturn(new PagedResult(List.of(), 0, 10, 0L, 0));

        io.restassured.RestAssured.given()
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/characters")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", equalTo(0))
            .body("size", equalTo(10))
            .body("totalElements", equalTo(0));
    }
}
