package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindByEmailIntegrationTest {

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserByEmail() {
        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"%s"}
                """.formatted(UserEntityProvider.EMAIL))
        .when()
            .post("/users/email-lookup")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
    }

    @Test
    void shouldReturn404WhenEmailNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"nonexistent@email.com"}
                """)
        .when()
            .post("/users/email-lookup")
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"not-an-email"}
                """)
        .when()
            .post("/users/email-lookup")
        .then()
            .statusCode(400);
    }
}
