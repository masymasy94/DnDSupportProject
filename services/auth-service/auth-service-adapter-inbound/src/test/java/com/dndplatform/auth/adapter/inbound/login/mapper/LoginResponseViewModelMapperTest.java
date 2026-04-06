package com.dndplatform.auth.adapter.inbound.login.mapper;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class LoginResponseViewModelMapperTest {

    private final LoginResponseViewModelMapper sut = Mappers.getMapper(LoginResponseViewModelMapper.class);

    @Test
    void shouldMapToCreateLoginTokensResponseViewModel(@Random CreateLoginTokenResponse response) {
        var result = sut.apply(response);

        assertThat(result.accessToken()).isEqualTo(response.accessToken());
        assertThat(result.refreshToken()).isEqualTo(response.refreshToken());
        assertThat(result.accessTokenExpiresAt()).isEqualTo(response.accessTokenExpiresAt());
        assertThat(result.refreshTokenExpiresAt()).isEqualTo(response.refreshTokenExpiresAt());
        assertThat(result.userId()).isEqualTo(response.userId());
    }
}
