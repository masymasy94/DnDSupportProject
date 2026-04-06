package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OtpLoginTokenCreateRepositoryJpaTest {

    @Mock
    private OtpLoginPanacheRepository panacheRepository;

    private OtpLoginTokenCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new OtpLoginTokenCreateRepositoryJpa(panacheRepository, 5);
    }

    @Test
    void shouldPersistEntityAndReturnTokenWithPlaintextOtpCode(@Random long userId) {
        willDoNothing().given(panacheRepository).persist(any(OtpLoginEntity.class));
        ArgumentCaptor<OtpLoginEntity> captor = ArgumentCaptor.forClass(OtpLoginEntity.class);

        var result = sut.create(userId);

        then(panacheRepository).should().persist(captor.capture());
        var persistedEntity = captor.getValue();
        assertThat(persistedEntity.userId).isEqualTo(userId);
        assertThat(persistedEntity.used).isFalse();
        assertThat(persistedEntity.token).isNotBlank();
        // The entity holds the hashed token; the returned domain model holds the raw OTP
        assertThat(result.token()).isNotEqualTo(persistedEntity.token);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.used()).isFalse();
    }
}
