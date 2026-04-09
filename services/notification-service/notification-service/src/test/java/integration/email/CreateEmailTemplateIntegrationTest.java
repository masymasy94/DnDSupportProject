package integration.email;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CreateEmailTemplateIntegrationTest {

    @Test
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldCreateEmailTemplate() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name":"welcome","subject":"Welcome!","htmlContent":"<html><body>Hi {name}</body></html>","description":"Welcome email"}
                """)
        .when()
            .post("/email-templates")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("name", org.hamcrest.Matchers.equalTo("welcome"));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name":"","subject":"Subject","htmlContent":"<html></html>","description":"desc"}
                """)
        .when()
            .post("/email-templates")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenHtmlContentIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name":"name","subject":"Subject","htmlContent":"","description":"desc"}
                """)
        .when()
            .post("/email-templates")
        .then()
            .statusCode(400);
    }
}
