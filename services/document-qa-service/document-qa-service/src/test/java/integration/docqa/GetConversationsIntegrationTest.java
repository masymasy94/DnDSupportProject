package integration.docqa;

import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class GetConversationsIntegrationTest {

    @InjectMock
    ConversationFindRepository repository;

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldListConversations() {
        var conversation = new Conversation(1L, 1L, null, "Test Conversation",
            LocalDateTime.now(), LocalDateTime.now());
        given(repository.findByUserId(anyLong())).willReturn(List.of(conversation));

        io.restassured.RestAssured.given()
            .queryParam("userId", 1)
        .when()
            .get("/api/document-qa/conversations")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(1))
            .body("[0].title", equalTo("Test Conversation"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoConversations() {
        given(repository.findByUserId(anyLong())).willReturn(List.of());

        io.restassured.RestAssured.given()
            .queryParam("userId", 1)
        .when()
            .get("/api/document-qa/conversations")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        io.restassured.RestAssured.given()
            .queryParam("userId", 1)
        .when()
            .get("/api/document-qa/conversations")
        .then()
            .statusCode(401);
    }
}
