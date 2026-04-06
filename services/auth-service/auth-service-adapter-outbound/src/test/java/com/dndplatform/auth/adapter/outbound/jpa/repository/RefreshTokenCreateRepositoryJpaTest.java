package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.RefreshTokenMapper;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RefreshTokenCreateRepositoryJpaTest {

    @Mock
    private RefreshTokenMapper mapper;

    @Mock
    private RefreshTokenPanacheRepository panacheRepository;

    private RefreshTokenCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshTokenCreateRepositoryJpa(mapper, panacheRepository, 30);
    }

    @Test
    void shouldPersistEntityAndReturnMappedToken(@Random long userId, @Random RefreshToken expected) {
        willDoNothing().given(panacheRepository).persist(any(RefreshTokenEntity.class));
        given(mapper.apply(any(RefreshTokenEntity.class))).willReturn(expected);

        var result = sut.createRefreshToken(userId);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().persist(any(RefreshTokenEntity.class));
    }

    @Test
    void shouldPersistEntityWithCorrectUserId(@Random long userId, @Random RefreshToken expected) {
        ArgumentCaptor<RefreshTokenEntity> captor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        willDoNothing().given(panacheRepository).persist(any(RefreshTokenEntity.class));
        given(mapper.apply(any(RefreshTokenEntity.class))).willReturn(expected);

        sut.createRefreshToken(userId);

        then(panacheRepository).should().persist(captor.capture());
        assertThat(captor.getValue().userId).isEqualTo(userId);
        assertThat(captor.getValue().revoked).isFalse();
        assertThat(captor.getValue().token).isNotBlank();
        assertThat(captor.getValue().expiresAt).isNotNull();
    }
}
