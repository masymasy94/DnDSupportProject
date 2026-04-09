package integration.chat;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class OnlineUsersFindIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptySetWhenNoUsersOnline() {
        // ChatSessionManager is an in-memory websocket session manager — no external state.
        // With no active websocket connections, the set is empty.

        // when / then
        given()
        .when()
                .get("/api/chat/online-users")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", equalTo(0));
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
        .when()
                .get("/api/chat/online-users")
        .then()
                .statusCode(401);
    }
}
