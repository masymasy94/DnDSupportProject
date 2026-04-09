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
class ConversationFindByUserIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(ConversationEntityProvider.class)
    @DeleteEntities(from = MessageEntity.class)
    @DeleteEntities(from = ConversationParticipantEntity.class)
    @DeleteEntities(from = ConversationEntity.class)
    void shouldFindConversationsByUser() {
        // when / then
        given()
                .queryParam("userId", ConversationEntityProvider.CREATOR_USER_ID)
        .when()
                .get("/api/chat/conversations")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyForUserWithNoConversations() {
        // when / then
        given()
                .queryParam("userId", 99_999L) // hardcoded: user id outside any seeded fixture
        .when()
                .get("/api/chat/conversations")
        .then()
                .statusCode(200)
                .contentType(JSON);
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/api/chat/conversations")
        .then()
                .statusCode(401);
    }
}
