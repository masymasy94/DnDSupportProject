package integration.compendium;

import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.EquipmentFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class EquipmentFindAllIntegrationTest {

    @InjectMock
    EquipmentFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedEquipments() {
        // given
        given(repository.findAllEquipment(any(EquipmentFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(
                        new Equipment(1, "Backpack", "Adventuring Gear", BigDecimal.valueOf(2), "2gp", BigDecimal.valueOf(5), "Carry stuff", null, SourceType.OFFICIAL, null, null, true) // hardcoded: deterministic seed for assertion
                ), 0, 20, 1L)); // hardcoded: page=0, size=20, total=1 — default pagination snapshot

        // when / then
        given()
        .when()
                .get("/api/compendium/equipment")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoEquipments() {
        // given
        given(repository.findAllEquipment(any(EquipmentFilterCriteria.class)))
                .willReturn(new PagedResult<>(List.of(), 0, 20, 0L)); // hardcoded: empty page snapshot

        // when / then
        given()
        .when()
                .get("/api/compendium/equipment")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/compendium/equipment")
        .then()
                .statusCode(401);
    }
}
