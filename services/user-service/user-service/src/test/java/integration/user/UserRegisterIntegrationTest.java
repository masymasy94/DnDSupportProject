package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserRegisterIntegrationTest {

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldRegisterNewUser() {
        var request = new UserRegisterViewModel("gandalf", "gandalf@shire.com", "MyPassword1");

        var response = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.username()).isEqualTo("gandalf");
        assertThat(response.email()).isEqualTo("gandalf@shire.com");
        assertThat(response.role()).isEqualTo("PLAYER");
        assertThat(response.active()).isTrue();
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void shouldReturn400WhenUsernameIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel("", "valid@email.com", "password"))
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenUsernameIsTooShort() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel("ab", "valid@email.com", "password"))
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel("validuser", "not-an-email", "password"))
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenPasswordIsBlank() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel("validuser", "valid@email.com", ""))
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn400WhenUsernameHasInvalidChars() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel("invalid user!", "valid@email.com", "password"))
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturn409WhenUsernameAlreadyExists() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel(UserEntityProvider.USERNAME, "different@email.com", "password"))
        .when()
            .post("/users")
        .then()
            .statusCode(409);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturn409WhenEmailAlreadyExists() {
        given()
            .contentType(ContentType.JSON)
            .body(new UserRegisterViewModel("differentuser", UserEntityProvider.EMAIL, "password"))
        .when()
            .post("/users")
        .then()
            .statusCode(409);
    }
}
