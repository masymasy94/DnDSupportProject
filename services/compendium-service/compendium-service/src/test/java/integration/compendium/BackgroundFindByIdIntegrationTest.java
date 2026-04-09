package integration.compendium;

import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindByIdRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class BackgroundFindByIdIntegrationTest {

    @InjectMock
    BackgroundFindByIdRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnBackgroundById() {
        given(repository.findById(anyInt())).willReturn(Optional.of(new Background(1, "Sage", "Scholar", SourceType.OFFICIAL, null, null, true)));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/backgrounds/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON).body("name", org.hamcrest.Matchers.equalTo("Sage"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn404WhenBackgroundNotFound() {
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/backgrounds/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/backgrounds/1")
        .then()
            .statusCode(401);
    }
}
