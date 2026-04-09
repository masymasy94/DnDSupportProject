package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserUpdatePasswordIntegrationTest {

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldUpdatePasswordSuccessfully() {
        // Register user
        var registered = given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"pwduser","email":"pwduser@test.com","password":"OldPassword1"}
                """)
        .when()
            .post("/users")
        .then()
            .statusCode(200)
            .extract().as(UserViewModel.class);

        // Update password
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"newPassword":"NewPassword1"}
                """)
        .when()
            .put("/users/" + registered.id() + "/password")
        .then()
            .statusCode(204);

        // Verify new password works
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"pwduser","password":"NewPassword1"}
                """)
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(200);
    }

    @Test
    void shouldReturn204EvenForNonexistentUser() {
        // Update password is fire-and-forget — no 404 for missing user
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"newPassword":"NewPassword1"}
                """)
        .when()
            .put("/users/999999/password")
        .then()
            .statusCode(204);
    }
}
