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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
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
        // default stub: empty list — overridden per test where needed
        given(documentListRepository.listAll()).willReturn(List.of());
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = DocumentMetadataEntity.class)
    void shouldListDocuments() {
        // given
        given(documentListRepository.listAll()).willReturn(List.of(
                new DocumentListItem("doc-1", "test.pdf") // hardcoded: deterministic doc id+name for assertions
        ));

        // when / then
        io.restassured.RestAssured.given()
        .when()
                .get("/api/assets/documents")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", greaterThanOrEqualTo(1))
                .body("fileName", hasItem(equalTo("test.pdf")));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoDocuments() {
        // when / then
        io.restassured.RestAssured.given()
        .when()
                .get("/api/assets/documents")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(0));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        io.restassured.RestAssured.given()
        .when()
                .get("/api/assets/documents")
        .then()
                .statusCode(401);
    }
}
