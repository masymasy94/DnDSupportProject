package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MagicItemFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class MagicItemFindAllIntegrationTest {

    @InjectMock
    MagicItemFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedMagicItems() {
        given(repository.findAllMagicItems(any(com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria.class)))
            .willReturn(new PagedResult<>(List.of(new MagicItem(1, "Bag of Holding", "Uncommon", "Wondrous Item", false, null, "Holds extradimensional space", null, SourceType.OFFICIAL, null, null, true)), 0, 20, 1L));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/magic-items")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoMagicItems() {
        given(repository.findAllMagicItems(any(com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria.class)))
            .willReturn(new PagedResult<>(List.of(), 0, 20, 0L));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/magic-items")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/magic-items")
        .then()
            .statusCode(401);
    }
}
