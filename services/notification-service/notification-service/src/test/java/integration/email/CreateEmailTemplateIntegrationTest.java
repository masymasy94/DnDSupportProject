package integration.email;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModelBuilder;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.NamedParam;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class CreateEmailTemplateIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateEmailTemplateRequestViewModel payloadTemplate;

    // Same field acts as:
    //   - value injected into the payload via .withName(name)
    //   - JPQL :name parameter for @DeleteEntities cleanup
    @NamedParam
    @InjectRandom
    private String name;

    @Test
    @DeleteEntities(from = EmailTemplateEntity.class, where = "e.name = :name", expectedRowCount = 1)
    void shouldCreateEmailTemplate() throws JsonProcessingException {
        // given
        var request = CreateEmailTemplateRequestViewModelBuilder.toBuilder(payloadTemplate)
                .withName(name)
                .withSubject("Welcome!") // hardcoded: arbitrary subject under @Size(max = 255)
                .withHtmlContent("<html><body>Hi {name}</body></html>") // hardcoded: must be non-blank
                .withDescription("Welcome email") // hardcoded: under @Size(max = 500)
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/email-templates")
        .then()
                .statusCode(201)
                .contentType(JSON)
                .body("name", equalTo(name));
    }

    @Test
    void shouldFailWhenNameIsBlank() throws JsonProcessingException {
        // given
        var request = CreateEmailTemplateRequestViewModelBuilder.toBuilder(payloadTemplate)
                .withName("") // hardcoded: triggers @NotBlank
                .withSubject("Subject") // hardcoded: arbitrary
                .withHtmlContent("<html></html>") // hardcoded: must be non-blank
                .withDescription("desc") // hardcoded: arbitrary
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/email-templates")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenHtmlContentIsBlank() throws JsonProcessingException {
        // given
        var request = CreateEmailTemplateRequestViewModelBuilder.toBuilder(payloadTemplate)
                .withName("name") // hardcoded: arbitrary, isolate failure to htmlContent
                .withSubject("Subject") // hardcoded: arbitrary
                .withHtmlContent("") // hardcoded: triggers @NotBlank
                .withDescription("desc") // hardcoded: arbitrary
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/email-templates")
        .then()
                .statusCode(400);
    }
}
