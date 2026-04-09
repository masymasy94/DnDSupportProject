package integration.user;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.test.entity.DeleteEntities;
import com.dndplatform.test.entity.DeleteEntitiesExtension;
import com.dndplatform.test.entity.PrepareEntities;
import com.dndplatform.test.entity.PrepareEntitiesExtension;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModelBuilder;
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
class UserCredentialsValidateIntegrationTest {

    @Inject
    ObjectMapper objectMapper;

    @InjectRandom
    private UserCredentialsValidateViewModel payloadTemplate;

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldValidateCorrectCredentials() throws JsonProcessingException {
        // given
        var request = UserCredentialsValidateViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(UserEntityProvider.USERNAME) // hardcoded: matches seeded user
                .withPassword(UserEntityProvider.PASSWORD) // hardcoded: matches seeded password
                .build();

        // when
        var response = given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users/credentials-validation")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().as(UserViewModel.class);

        // then
        assertThat(response.username()).isEqualTo(UserEntityProvider.USERNAME);
        assertThat(response.email()).isEqualTo(UserEntityProvider.EMAIL);
    }

    @Test
    @PrepareEntities(UserEntityProvider.class)
    @DeleteEntities(from = UserEntity.class)
    void shouldFailForWrongPassword() throws JsonProcessingException {
        // given
        var request = UserCredentialsValidateViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername(UserEntityProvider.USERNAME) // hardcoded: matches seeded user
                .withPassword("WrongPassword") // hardcoded: must NOT match the seeded password
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users/credentials-validation")
        .then()
                .statusCode(401);
    }

    @Test
    void shouldFailForNonexistentUser() throws JsonProcessingException {
        // given
        var request = UserCredentialsValidateViewModelBuilder.toBuilder(payloadTemplate)
                .withUsername("nonexistentuser") // hardcoded: must not exist in DB
                .withPassword("password") // hardcoded: arbitrary, not relevant since user is missing
                .build();

        // when / then
        given()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/users/credentials-validation")
        .then()
                .statusCode(401);
    }
}
