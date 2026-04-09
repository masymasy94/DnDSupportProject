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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class ConversationUpdateReadByIdIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(ConversationEntityProvider.class)
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldMarkConversationAsRead() {
        var conversationId = given()
            .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
            .get("/api/chat/conversations")
        .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("[0].id");

        given()
            .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
            .put("/api/chat/conversations/" + conversationId + "/read")
        .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnErrorWhenConversationNotFound() {
        // Chat-service maps "not found" exceptions to 400 (not 404)
        given()
            .queryParam("userId", 1)
        .when()
            .put("/api/chat/conversations/999999/read")
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
            .put("/api/chat/conversations/1/read")
        .then()
            .statusCode(401);
    }
}
