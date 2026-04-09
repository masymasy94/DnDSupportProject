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
class UserFindByIdIntegrationTest {

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserById() {
        // Register via API to get a known ID
        var registered = given()
            .contentType(ContentType.JSON)
            .body("""
                {"username":"findme","email":"findme@test.com","password":"pass123"}
                """)
        .when()
            .post("/users")
        .then()
            .statusCode(200)
            .extract().as(UserViewModel.class);

        var response = given()
        .when()
            .get("/users/" + registered.id())
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(UserViewModel.class);

        assertThat(response.id()).isEqualTo(registered.id());
        assertThat(response.username()).isEqualTo("findme");
        assertThat(response.email()).isEqualTo("findme@test.com");
    }

    @Test
    void shouldReturn404WhenUserNotFound() {
        given()
        .when()
            .get("/users/999999")
        .then()
            .statusCode(404);
    }
}
