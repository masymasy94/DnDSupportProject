package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModelBuilder;
import com.dndplatform.user.view.model.vm.UserViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith({RandomExtension.class, PrepareEntitiesExtension.class, DeleteEntitiesExtension.class})
class UserFindByEmailIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private UserFindByEmailViewModel payloadTemplate;

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFindUserByEmail() throws JsonProcessingException {
        // given
        var request = UserFindByEmailViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail(UserEntityProvider.EMAIL) // hardcoded: matches seeded user
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users/email-lookup")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
    }

    @Test
    void shouldFailWhenEmailNotFound() throws JsonProcessingException {
        // given
        var request = UserFindByEmailViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("nonexistent@email.com") // hardcoded: must not match any user
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users/email-lookup")
        .then()
                .statusCode(404);
    }

    @Test
    void shouldFailWhenEmailIsInvalid() throws JsonProcessingException {
        // given
        var request = UserFindByEmailViewModelBuilder.toBuilder(payloadTemplate)
                .withEmail("not-an-email") // hardcoded: triggers @Email validation
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users/email-lookup")
        .then()
                .statusCode(400);
    }
}
