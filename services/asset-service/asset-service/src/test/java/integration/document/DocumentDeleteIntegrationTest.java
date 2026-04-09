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
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.anyString;

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
        Mockito.doNothing().when(minioDeleteRepository).delete(anyString());

        given()
        .when()
            .delete("/api/assets/documents/" + DocumentMetadataEntityProvider.ID)
        .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn204EvenForNonexistentDocument() {
        // DocumentDeleteServiceImpl is fire-and-forget — no 404 for missing IDs.
        // Both MinIO and metadata delete are best-effort.
        Mockito.doNothing().when(minioDeleteRepository).delete(anyString());

        given()
        .when()
            .delete("/api/assets/documents/nonexistent-id")
        .then()
            .statusCode(204);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .delete("/api/assets/documents/some-id")
        .then()
            .statusCode(401);
    }
}
