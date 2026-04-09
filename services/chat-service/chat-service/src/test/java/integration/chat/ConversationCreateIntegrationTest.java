package integration.chat;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class ConversationCreateIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldCreateGroupConversation() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .body("""
                {"type":"GROUP","name":"Party Chat","participantIds":[2,3]}
                """)
        .when()
            .post("/api/chat/conversations")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(201)));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldCreateDirectConversation() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .body("""
                {"type":"DIRECT","participantIds":[2]}
                """)
        .when()
            .post("/api/chat/conversations")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(201)));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenTypeIsMissing() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .body("""
                {"participantIds":[2]}
                """)
        .when()
            .post("/api/chat/conversations")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .body("""
                {"type":"DIRECT","participantIds":[2]}
                """)
        .when()
            .post("/api/chat/conversations")
        .then()
            .statusCode(401);
    }
}
