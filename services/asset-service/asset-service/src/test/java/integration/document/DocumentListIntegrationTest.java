package integration.document;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.DocumentListItem;
import com.dndplatform.asset.domain.repository.DocumentListRepository;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class DocumentListIntegrationTest {

    @InjectMock
    DocumentListRepository documentListRepository;

    @BeforeEach
    void setUp() {
        // Default: empty list
        given(documentListRepository.listAll()).willReturn(List.of());
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = DocumentMetadataEntity.class)
    void shouldListDocuments() {
        given(documentListRepository.listAll()).willReturn(List.of(
            new DocumentListItem("doc-1", "test.pdf")
        ));

        io.restassured.RestAssured.given()
        .when()
            .get("/api/assets/documents")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThanOrEqualTo(1))
            .body("fileName", hasItem(equalTo("test.pdf")));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoDocuments() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/assets/documents")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
        .when()
            .get("/api/assets/documents")
        .then()
            .statusCode(401);
    }
}
