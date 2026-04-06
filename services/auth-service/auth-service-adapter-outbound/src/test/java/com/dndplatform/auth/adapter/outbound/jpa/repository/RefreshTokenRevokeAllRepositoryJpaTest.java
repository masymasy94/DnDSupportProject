package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class RefreshTokenRevokeAllRepositoryJpaTest {

    @Mock
    private RefreshTokenPanacheRepository panacheRepository;

    private RefreshTokenRevokeAllRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshTokenRevokeAllRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldIssueCorrectUpdateQueryForAllUserTokens(@Random long userId) {
        sut.revokeAllTokens(userId);

        then(panacheRepository).should(times(1))
                .update("revoked = true where userId = ?1", userId);
    }
}
