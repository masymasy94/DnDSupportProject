package integration.compendium;

import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindAllRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class LanguageFindAllIntegrationTest {

    @InjectMock
    LanguageFindAllRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnAllLanguages() {
        given(repository.findAllLanguages()).willReturn(List.of(new Language((short) 1, "Common", "Latin", "Standard")));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/languages")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1)).body("name", org.hamcrest.Matchers.hasItem("Common"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoLanguages() {
        given(repository.findAllLanguages()).willReturn(List.of());

        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/languages")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/compendium/languages")
        .then()
            .statusCode(401);
    }
}
