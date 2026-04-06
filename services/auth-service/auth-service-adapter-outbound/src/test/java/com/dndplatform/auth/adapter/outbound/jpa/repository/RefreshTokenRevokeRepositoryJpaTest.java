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
class RefreshTokenRevokeRepositoryJpaTest {

    @Mock
    private RefreshTokenPanacheRepository panacheRepository;

    private RefreshTokenRevokeRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshTokenRevokeRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldIssueCorrectUpdateQueryWithTokenAndUserId(@Random String token, @Random long userId) {
        sut.revokeToken(token, userId);

        then(panacheRepository).should(times(1))
                .update("revoked = true where token = ?1 and userId = ?2", token, userId);
    }
}
