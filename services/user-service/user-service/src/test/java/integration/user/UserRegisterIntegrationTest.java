package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.NamedParam;
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
class UserRegisterIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private UserRegisterViewModel payloadTemplate;

    // Same field acts as:
    //   - value injected into the payload via .withUsername(username)
    //   - JPQL :username parameter for @DeleteEntities cleanup
    @NamedParam
    private String username = "cleanup-fallback";

    @Test
    @DeleteEntities(from = UserEntity.class, where = "e.username = :username", expectedRowCount = 1)
    void shouldRegisterNewUser() throws JsonProcessingException {
        // given
        // hardcoded username: random Instancio strings can violate @Pattern (^[a-zA-Z0-9_]+$),
        // so we build a UUID-based safe username and re-use it for both the payload and the
        // @DeleteEntities :username binding
        username = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(username)
                .withEmail("user-" + username + "@example.com") // hardcoded shape: must satisfy @Email
                .withPassword("MyPassword1") // hardcoded: simple non-blank password
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .contentType(JSON)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.email()).isEqualTo("user-" + username + "@example.com");
        assertThat(response.role()).isEqualTo("PLAYER");
        assertThat(response.active()).isTrue();
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void shouldFailWhenUsernameIsBlank() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("") // hardcoded: triggers @NotBlank
                .withEmail("valid@email.com") // hardcoded: isolate failure to username
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenUsernameIsTooShort() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("ab") // hardcoded: below @Size(min = 3)
                .withEmail("valid@email.com") // hardcoded: isolate failure to username length
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenEmailIsInvalid() throws JsonProcessingException {
        // given
        var safeUsername = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(safeUsername) // hardcoded: random Instancio string may violate @Pattern
                .withEmail("not-an-email") // hardcoded: triggers @Email validation
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenPasswordIsBlank() throws JsonProcessingException {
        // given
        var safeUsername = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(safeUsername) // hardcoded: random Instancio string may violate @Pattern
                .withEmail("valid@email.com") // hardcoded: isolate failure to password
                .withPassword("") // hardcoded: triggers @NotBlank
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    void shouldFailWhenUsernameHasInvalidChars() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("invalid user!") // hardcoded: contains space + ! to fail @Pattern
                .withEmail("valid@email.com") // hardcoded: isolate failure to username pattern
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFailWhenUsernameAlreadyExists() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(UserEntityProvider.USERNAME) // hardcoded: matches seeded user
                .withEmail("different@email.com") // hardcoded: isolate the conflict to username
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(409);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFailWhenEmailAlreadyExists() throws JsonProcessingException {
        // given
        var request = UserRegisterViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("differentuser") // hardcoded: must differ from seeded user
                .withEmail(UserEntityProvider.EMAIL) // hardcoded: matches seeded user
                .withPassword("MyPassword1") // hardcoded: non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users")
        .then()
                .statusCode(409);
    }
}
