package integration.docqa;

import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
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
        // given
        var conversation = new Conversation(
                1L, // hardcoded: deterministic id
                1L, // hardcoded: matches @TestSecurity user
                null,
                "Test Conversation", // hardcoded: deterministic title for assertion
                LocalDateTime.now(),
                LocalDateTime.now());
        given(repository.findByUserId(anyLong())).willReturn(List.of(conversation));

        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/api/document-qa/conversations")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(1))
                .body("[0].title", equalTo("Test Conversation"));
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyListWhenNoConversations() {
        // given
        given(repository.findByUserId(anyLong())).willReturn(List.of());

        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: matches @TestSecurity user
        .when()
                .get("/api/document-qa/conversations")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(0));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("userId", 1) // hardcoded: arbitrary, auth fails first
        .when()
                .get("/api/document-qa/conversations")
        .then()
                .statusCode(401);
    }
}
