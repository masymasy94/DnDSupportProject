package integration.chat;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModel;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModelBuilder;
import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class ConversationCreateIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private CreateConversationViewModel payloadTemplate;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldCreateGroupConversation() throws JsonProcessingException {
        // given
        var request = CreateConversationViewModelBuilder.toBuilder(payloadTemplate)
                .withType("GROUP") // hardcoded: enum-like product field, validated against allowed values
                .withName("Party Chat") // hardcoded: required for GROUP type
                .withParticipantIds(List.of(2L, 3L)) // hardcoded: deterministic participants for the group
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations")
        .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldCreateDirectConversation() throws JsonProcessingException {
        // given
        var request = CreateConversationViewModelBuilder.toBuilder(payloadTemplate)
                .withType("DIRECT") // hardcoded: enum-like product field
                .withName(null) // hardcoded: ignored for DIRECT, sent as null to be explicit
                .withParticipantIds(List.of(2L)) // hardcoded: single participant for DIRECT
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations")
        .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenTypeIsMissing() throws JsonProcessingException {
        // given
        var request = CreateConversationViewModelBuilder.toBuilder(payloadTemplate)
                .withType(null) // hardcoded: triggers @NotNull on type
                .withParticipantIds(List.of(2L)) // hardcoded: arbitrary, isolate failure to type
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations")
        .then()
                .statusCode(400)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() throws JsonProcessingException {
        // given
        var request = CreateConversationViewModelBuilder.toBuilder(payloadTemplate)
                .withType("DIRECT") // hardcoded: arbitrary valid type, auth fails first
                .withParticipantIds(List.of(2L)) // hardcoded: arbitrary, auth fails first
                .build();

        // when / then
        given()
                .contentType(JSON)
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/api/chat/conversations")
        .then()
                .statusCode(401);
    }
}
