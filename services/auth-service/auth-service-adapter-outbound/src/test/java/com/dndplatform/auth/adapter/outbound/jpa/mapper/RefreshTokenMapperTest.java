package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RefreshTokenMapperTest {

    private final RefreshTokenMapper sut = new RefreshTokenMapperImpl();

    @Test
    void shouldMapAllFieldsFromEntityToDomain(@Random RefreshTokenEntity entity) {
        var result = sut.apply(entity);

        assertThat(result.token()).isEqualTo(entity.getToken());
        assertThat(result.userId()).isEqualTo(entity.getUserId());
        assertThat(result.expiresAt()).isEqualTo(entity.getExpiresAt());
        assertThat(result.revoked()).isEqualTo(entity.getRevoked());
        assertThat(result.createdAt()).isEqualTo(entity.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        var result = sut.apply(null);

        assertThat(result).isNull();
    }
}
