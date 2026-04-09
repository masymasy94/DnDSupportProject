package integration.chat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class OnlineUsersFindIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptySetWhenNoUsersOnline() {
        // ChatSessionManager is an in-memory websocket session manager — no external state.
        // With no active websocket connections, the set is empty.
        given()
        .when()
            .get("/api/chat/online-users")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
        .when()
            .get("/api/chat/online-users")
        .then()
            .statusCode(401);
    }
}
