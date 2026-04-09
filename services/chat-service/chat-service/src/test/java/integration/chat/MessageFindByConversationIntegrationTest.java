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
class MessageFindByConversationIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(ConversationEntityProvider.class)
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldListMessagesInConversation() {
        var conversationId = given()
            .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
            .get("/api/chat/conversations")
        .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("[0].id");

        given()
            .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
            .queryParam("page", 0)
            .queryParam("pageSize", 20)
        .when()
            .get("/api/chat/conversations/" + conversationId + "/messages")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnErrorWhenConversationNotFound() {
        // Chat-service maps "not found" exceptions to 400 (not 404)
        given()
            .queryParam("userId", 1)
            .queryParam("page", 0)
            .queryParam("pageSize", 20)
        .when()
            .get("/api/chat/conversations/999999/messages")
        .then()
            .statusCode(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo(400),
                org.hamcrest.Matchers.equalTo(404)));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("userId", 1)
        .when()
            .get("/api/chat/conversations/1/messages")
        .then()
            .statusCode(401);
    }
}
