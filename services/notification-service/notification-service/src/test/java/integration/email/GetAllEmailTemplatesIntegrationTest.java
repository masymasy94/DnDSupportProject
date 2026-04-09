package integration.email;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class GetAllEmailTemplatesIntegrationTest {

    @Test
    @PrepareEntities(EmailTemplateEntityProvider.class)
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldReturnAllTemplates() {
        // when / then
        given()
        .when()
                .get("/email-templates")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("templates", not(empty()));
    }

    @Test
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldReturnEmptyListWhenNoTemplates() {
        // when / then
        given()
        .when()
                .get("/email-templates")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }
}
