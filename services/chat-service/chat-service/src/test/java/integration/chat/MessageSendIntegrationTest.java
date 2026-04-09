package integration.chat;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class MessageSendIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(ConversationEntityProvider.class)
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldSendMessage() {
        var conversationId = given()
            .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
            .get("/api/chat/conversations")
        .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("[0].id");

        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
            .body("""
                {"content":"Hello!","messageType":"TEXT"}
                """)
        .when()
            .post("/api/chat/conversations/" + conversationId + "/messages")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(200),
                org.hamcrest.Matchers.equalTo(201)));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturn400WhenContentIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .body("""
                {"content":"","messageType":"TEXT"}
                """)
        .when()
            .post("/api/chat/conversations/1/messages")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .body("""
                {"content":"Hello","messageType":"TEXT"}
                """)
        .when()
            .post("/api/chat/conversations/1/messages")
        .then()
            .statusCode(401);
    }
}
