package integration.compendium;

import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.MagicItemFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class MagicItemFindAllIntegrationTest {

    @InjectMock
    MagicItemFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedMagicItems() {
        // given
        given(repository.findAllMagicItems(any(MagicItemFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(
                        new MagicItem(1, "Bag of Holding", "Uncommon", "Wondrous Item", false, null, "Holds extradimensional space", null, SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
                ), 0, 20, 1L)); // hardcoded: page=0, size=20, total=1 — default pagination snapshot

        // when / then
        given()
        .when()
                .get("/api/compendium/magic-items")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoMagicItems() {
        // given
        given(repository.findAllMagicItems(any(MagicItemFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(), 0, 20, 0L)); // hardcoded: empty page snapshot

        // when / then
        given()
        .when()
                .get("/api/compendium/magic-items")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/compendium/magic-items")
        .then()
                .statusCode(401);
    }
}
