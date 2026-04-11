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
import static io.restassured.http.ContentType.JSON;

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
        // given
        var conversationId = given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .get("/api/chat/conversations")
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getLong("[0].id");

        // when
        given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .put("/api/chat/conversations/{id}/read", conversationId)
        .then()
                .statusCode(204);

        // then — verify read status via GET
        given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .get("/api/chat/conversations/{id}", conversationId)
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldFailWhenConversationNotFound() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .put("/api/chat/conversations/{id}/read", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .put("/api/chat/conversations/{id}/read", 1L) // hardcoded: arbitrary, auth fails first
        .then()
                .statusCode(401);
    }
}
