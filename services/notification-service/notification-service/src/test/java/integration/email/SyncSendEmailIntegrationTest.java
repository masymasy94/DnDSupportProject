package integration.email;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModelBuilder;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class SyncSendEmailIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private EmailSendRequestViewModel payloadTemplate;

    @Test
    @PrepareEntities(EmailTemplateEntityProvider.class)
    @DeleteEntities(from = EmailTemplateEntity.class)
    void shouldSendEmailUsingTemplate() throws JsonProcessingException {
        // given
        var templateId = given()
        .when()
                .get("/email-templates")
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getLong("templates[0].id");

        var request = EmailSendRequestViewModelBuilder.toBuilder(payloadTemplate)
                .withTo("user@example.com") // hardcoded: must satisfy @Email
                .withCc(null) // hardcoded: random Instancio strings are not valid emails
                .withBcc(null) // hardcoded: random Instancio strings are not valid emails
                .withTemplateId(templateId)
                .withTemplateVariables(Map.of("name", "John")) // hardcoded: matches the {name} placeholder in seeded template
                .withAttachments(null) // hardcoded: avoid random Instancio attachments triggering serialization edge cases
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/emails")
        .then()
                .statusCode(202);
    }

    @Test
    void shouldFailWhenRecipientIsInvalid() throws JsonProcessingException {
        // given
        var request = EmailSendRequestViewModelBuilder.toBuilder(payloadTemplate)
                .withTo("not-an-email") // hardcoded: triggers @Email validation
                .withCc(null) // hardcoded: random Instancio strings are not valid emails
                .withBcc(null) // hardcoded: random Instancio strings are not valid emails
                .withTemplateId(1L) // hardcoded: arbitrary, validation fails before lookup
                .withAttachments(null) // hardcoded: avoid serializer edge cases
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/emails")
        .then()
                .statusCode(400)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenTemplateIdIsMissing() throws JsonProcessingException {
        // given
        var request = EmailSendRequestViewModelBuilder.toBuilder(payloadTemplate)
                .withTo("user@example.com") // hardcoded: valid email
                .withCc(null) // hardcoded: random Instancio strings are not valid emails
                .withBcc(null) // hardcoded: random Instancio strings are not valid emails
                .withTemplateId(null) // hardcoded: triggers @NotNull on templateId
                .withAttachments(null) // hardcoded: avoid serializer edge cases
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/emails")
        .then()
                .statusCode(400)
                .contentType(JSON);
    }
}
