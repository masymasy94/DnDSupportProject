package integration.user;

import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserSearchIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldSearchByUsername() {
        // when
        var response = given()
                .queryParam("query", UserEntityProvider.USERNAME) // hardcoded: matches seeded user
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/users/search")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(PagedUserViewModel.class);

        // then
        assertThat(response.content()).isNotEmpty();
        assertThat(response.content().getFirst().username()).isEqualTo(UserEntityProvider.USERNAME);
    }

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    void shouldReturnEmptyForNoMatch() {
        // when
        var response = given()
                .queryParam("query", "nonexistentuser12345") // hardcoded: must not match any seed
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/users/search")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(PagedUserViewModel.class);

        // then
        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("query", "test") // hardcoded: arbitrary query value, not relevant for 401
        .when()
                .get("/users/search")
        .then()
                .statusCode(401);
    }
}
