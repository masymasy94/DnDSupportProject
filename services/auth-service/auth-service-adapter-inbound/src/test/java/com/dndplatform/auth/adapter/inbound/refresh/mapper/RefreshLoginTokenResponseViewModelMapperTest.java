package com.dndplatform.auth.adapter.inbound.refresh.mapper;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class RefreshLoginTokenResponseViewModelMapperTest {

    private final RefreshLoginTokenResponseViewModelMapper sut = Mappers.getMapper(RefreshLoginTokenResponseViewModelMapper.class);

    @Test
    void shouldMapToRefreshLoginTokenResponseViewModel(@Random CreateLoginTokenResponse response) {
        var result = sut.apply(response);

        assertThat(result.accessToken()).isEqualTo(response.accessToken());
        assertThat(result.accessTokenExpiresAt()).isEqualTo(response.accessTokenExpiresAt());
    }
}
