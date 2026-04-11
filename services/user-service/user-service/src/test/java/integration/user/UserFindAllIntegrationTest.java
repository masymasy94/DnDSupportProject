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
import org.assertj.core.api.SoftAssertions;

@QuarkusTest
@ExtendWith({PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindAllIntegrationTest {

    @Test
    @TestSecurity(user = "1", roles = "PLAYER")
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldReturnPagedUsers() {
        // when
        var response = given()
                .queryParam("page", 0) // hardcoded: first page of pagination
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(PagedUserViewModel.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.content()).isNotEmpty();
            softly.assertThat(response.page()).isZero();
            softly.assertThat(response.size()).isEqualTo(10);
            softly.assertThat(response.totalElements()).isGreaterThanOrEqualTo(1);
        });
    }

    @Test
    void shouldFailWhenNotAuthenticated() {
        // when / then
        given()
                .queryParam("page", 0) // hardcoded: first page
                .queryParam("size", 10) // hardcoded: arbitrary page size
        .when()
                .get("/users")
        .then()
                .statusCode(401);
    }
}
