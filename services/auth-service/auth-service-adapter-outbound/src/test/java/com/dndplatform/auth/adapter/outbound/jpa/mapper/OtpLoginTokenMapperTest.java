package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OtpLoginTokenMapperTest {

    private final OtpLoginTokenMapper sut = new OtpLoginTokenMapperImpl();

    @Test
    void shouldMapAllFieldsFromEntityToDomain(@Random OtpLoginEntity entity) {
        var result = sut.apply(entity);

        assertThat(result.id()).isEqualTo(entity.id);
        assertThat(result.token()).isEqualTo(entity.getToken());
        assertThat(result.userId()).isEqualTo(entity.getUserId());
        assertThat(result.expiresAt()).isEqualTo(entity.getExpiresAt());
        assertThat(result.used()).isEqualTo(entity.getUsed());
        assertThat(result.createdAt()).isEqualTo(entity.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        var result = sut.apply(null);

        assertThat(result).isNull();
    }
}
