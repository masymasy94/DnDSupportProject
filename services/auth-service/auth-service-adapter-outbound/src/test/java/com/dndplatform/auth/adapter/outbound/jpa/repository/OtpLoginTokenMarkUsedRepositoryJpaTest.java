package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OtpLoginTokenMarkUsedRepositoryJpaTest {

    @Mock
    private OtpLoginPanacheRepository panacheRepository;

    private OtpLoginTokenMarkUsedRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new OtpLoginTokenMarkUsedRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldIssueCorrectUpdateQueryWithHashedToken(@Random String rawToken) {
        var hashedToken = TokenUtil.sha256(rawToken);

        sut.markUsed(rawToken);

        then(panacheRepository).should(times(1))
                .update("used = true where token = ?1", hashedToken);
    }

    @Test
    void shouldHashRawTokenBeforeUpdating(@Random String rawToken) {
        var hashedToken = TokenUtil.sha256(rawToken);

        sut.markUsed(rawToken);

        assertThat(hashedToken).isNotEqualTo(rawToken);
        then(panacheRepository).should().update("used = true where token = ?1", hashedToken);
    }
}
