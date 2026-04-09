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
class UserCredentialsValidateIntegrationTest {

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldValidateCorrectCredentials() {
        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"%s","password":"%s"}
                """.formatted(UserEntityProvider.USERNAME, UserEntityProvider.PASSWORD))
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturn401ForWrongPassword() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"%s","password":"WrongPassword"}
                """.formatted(UserEntityProvider.USERNAME))
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(401);
    }

    @Test
    void shouldReturn401ForNonexistentUser() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"nonexistentuser","password":"password"}
                """)
        .when()
            .post("/users/credentials-validation")
        .then()
            .statusCode(401);
    }
}
