package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModelBuilder;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserRegisterViewModelBuilder;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModelBuilder;
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

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserUpdatePasswordIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private UserRegisterViewModel registerTemplate;

    @InjectRandom
    private UserUpdatePasswordViewModel updateTemplate;

    @InjectRandom
    private UserCredentialsValidateViewModel validateTemplate;

    @Test
    @DeleteEntities(from = UserEntity.class)
    void shouldUpdatePasswordSuccessfully() throws JsonProcessingException {
        // given
        // hardcoded username: random Instancio strings can violate @Pattern
        var safeUsername = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        var registerRequest = UserRegisterViewModelBuilder.toBuilder(registerTemplate)
                .withUsername(safeUsername)
                .withEmail(safeUsername + "@example.com") // hardcoded shape: must satisfy @Email
                .withPassword("OldPassword1") // hardcoded: known password to be replaced
                .build();
        var registered = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(registerRequest))
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .extract().as(UserViewModel.class);

        var updateRequest = UserUpdatePasswordViewModelBuilder.toBuilder(updateTemplate)
                .withNewPassword("NewPassword1") // hardcoded: known new password
                .build();

        // when
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(updateRequest))
        .when()
                .put("/users/{id}/password", registered.id())
        .then()
                .statusCode(204);

        // then
        var validateRequest = UserCredentialsValidateViewModelBuilder.toBuilder(validateTemplate)
                .withUsername(safeUsername) // hardcoded: matches the registered user
                .withPassword("NewPassword1") // hardcoded: must match the new password set above
                .build();
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(validateRequest))
        .when()
                .post("/users/credentials-validation")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldSucceedSilentlyForNonexistentUser() throws JsonProcessingException {
        // given
        // The update-password endpoint is fire-and-forget by design — no 404 for missing user.
        var updateRequest = UserUpdatePasswordViewModelBuilder.toBuilder(updateTemplate)
                .withNewPassword("NewPassword1") // hardcoded: arbitrary non-blank password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(updateRequest))
        .when()
                .put("/users/{id}/password", 999_999L) // hardcoded: id outside any seeded fixture
        .then()
                .statusCode(204);
    }
}
