package integration.email;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class GetAllEmailTemplatesIntegrationTest {

    @Test
    @PrepareEntities(EmailTemplateEntityProvider.class)
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldReturnAllTemplates() {
        given()
        .when()
            .get("/email-templates")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("templates", org.hamcrest.Matchers.not(org.hamcrest.Matchers.empty()));
    }

    @Test
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldReturnEmptyListWhenNoTemplates() {
        given()
        .when()
            .get("/email-templates")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }
}
