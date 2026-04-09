package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserSearchIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldSearchByUsername() {
        var response = given()
            .queryParam("query", UserEntityProvider.USERNAME)
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(PagedUserViewModel.class);

        assertThat(response.content()).isNotEmpty();
        assertThat(response.content().getFirst().username()).isEqualTo(UserEntityProvider.USERNAME);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyForNoMatch() {
        var response = given()
            .queryParam("query", "nonexistentuser12345")
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().as(PagedUserViewModel.class);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given()
            .queryParam("query", "test")
        .when()
            .get("/users/search")
        .then()
            .statusCode(401);
    }
}
