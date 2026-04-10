package integration.chat;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.view.model.vm.SendMessageViewModel;
import com.dndplatform.chat.view.model.vm.SendMessageViewModelBuilder;
import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class MessageSendIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private SendMessageViewModel payloadTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(ConversationEntityProvider.class)
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldSendMessage() throws JsonProcessingException {
        // given
        var conversationId = given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .get("/api/chat/conversations")
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getLong("[0].id");

        var request = SendMessageViewModelBuilder.toBuilder(payloadTemplate)
                .withContent("Hello!") // hardcoded: must be non-blank
                .withMessageType("TEXT") // hardcoded: enum-like product field
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations/{id}/messages", conversationId)
        .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenContentIsBlank() throws JsonProcessingException {
        // given
        var request = SendMessageViewModelBuilder.toBuilder(payloadTemplate)
                .withContent("") // hardcoded: triggers @NotBlank
                .withMessageType("TEXT") // hardcoded: arbitrary valid type
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations/{id}/messages", 1L) // hardcoded: arbitrary, validation fails first
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenNotAuthenticated() throws JsonProcessingException {
        // given
        var request = SendMessageViewModelBuilder.toBuilder(payloadTemplate)
                .withContent("Hello") // hardcoded: arbitrary content, auth fails first
                .withMessageType("TEXT") // hardcoded: arbitrary type, auth fails first
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations/{id}/messages", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
