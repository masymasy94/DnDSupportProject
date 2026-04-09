package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Equipment;
import java.math.BigDecimal;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.EquipmentFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class EquipmentFindAllIntegrationTest {

    @InjectMock
    EquipmentFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnPagedEquipments() {
        given(repository.findAllEquipment(any(com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria.class)))
            .willReturn(new PagedResult<>(List.of(new Equipment(1, "Backpack", "Adventuring Gear", BigDecimal.valueOf(2), "2gp", BigDecimal.valueOf(5), "Carry stuff", null, SourceType.OFFICIAL, null, null, true)), 0, 20, 1L));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/equipment")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyPagedResultWhenNoEquipments() {
        given(repository.findAllEquipment(any(com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria.class)))
            .willReturn(new PagedResult<>(List.of(), 0, 20, 0L));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/equipment")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/equipment")
        .then()
            .statusCode(401);
    }
}
