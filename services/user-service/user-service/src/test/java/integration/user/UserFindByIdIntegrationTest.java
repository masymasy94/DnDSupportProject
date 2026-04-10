package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserRegisterViewModelBuilder;
import com.dndplatform.user.view.model.vm.UserViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindByIdIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private UserRegisterViewModel registerTemplate;

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserById() throws JsonProcessingException {
        // given
        // hardcoded username: random Instancio strings can violate @Pattern (^[a-zA-Z0-9_]+$)
        var safeUsername = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        var registerRequest = UserRegisterViewModelBuilder.toBuilder(registerTemplate)
                .withUsername(safeUsername)
                .withEmail(safeUsername + "@example.com") // hardcoded shape: must satisfy @Email
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();
        var registered = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(registerRequest))
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .extract().as(UserViewModel.class);

        // when
        var response = given()
        .when()
                .get("/users/{id}", registered.id())
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.id()).isEqualTo(registered.id());
        assertThat(response.username()).isEqualTo(safeUsername);
        assertThat(response.email()).isEqualTo(safeUsername + "@example.com");
    }

    @Test
    void shouldFailWhenUserNotFound() {
        // when / then
        given()
        .when()
                .get("/users/{id}", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(404);
    }
}
