package integration.document;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.repository.DocumentDeleteRepository;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class DocumentDeleteIntegrationTest {

    @InjectMock
    DocumentDeleteRepository minioDeleteRepository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(DocumentMetadataEntityProvider.class)
    @DeleteEntities(from = DocumentMetadataEntity.class)
    void shouldDeleteExistingDocument() {
        // given
        doNothing().when(minioDeleteRepository).delete(anyString());

        // when / then
        given()
        .when()
                .delete("/api/assets/documents/{id}", DocumentMetadataEntityProvider.ID) // hardcoded: matches seeded entity ID
        .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldSucceedSilentlyForNonexistentDocument() {
        // given
        // DocumentDeleteServiceImpl is fire-and-forget — no 404 for missing IDs.
        // Both MinIO and metadata delete are best-effort.
        doNothing().when(minioDeleteRepository).delete(anyString());

        // when / then
        given()
        .when()
                .delete("/api/assets/documents/{id}", "nonexistent-id") // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(204);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .delete("/api/assets/documents/{id}", "some-id") // hardcoded: arbitrary id, auth fails first
        .then()
                .statusCode(401);
    }
}
