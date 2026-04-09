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
class SyncSendEmailIntegrationTest {

    @Test
    @PrepareEntities(EmailTemplateEntityProvider.class)
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldSendEmailUsingTemplate() {
        // Get template ID first
        var templateId = given()
        .when()
            .get("/email-templates")
        .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("templates[0].id");

        given()
            .contentType(ContentType.JSON)
            .body("""
                {"to":"user@example.com","templateId":%d,"templateVariables":{"name":"John"}}
                """.formatted(templateId))
        .when()
            .post("/emails")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(201),
                org.hamcrest.Matchers.equalTo(202)));
    }

    @Test
    void shouldReturn400WhenRecipientIsInvalid() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"to":"not-an-email","templateId":1}
                """)
        .when()
            .post("/emails")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenTemplateIdIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"to":"user@example.com"}
                """)
        .when()
            .post("/emails")
        .then()
            .statusCode(400);
    }
}
