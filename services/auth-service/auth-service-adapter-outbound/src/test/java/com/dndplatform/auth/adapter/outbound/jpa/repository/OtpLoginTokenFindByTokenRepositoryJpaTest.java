package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.OtpLoginTokenMapper;
import com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OtpLoginTokenFindByTokenRepositoryJpaTest {

    @Mock
    private OtpLoginTokenMapper mapper;

    @Mock
    private OtpLoginPanacheRepository panacheRepository;

    private OtpLoginTokenFindByTokenRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new OtpLoginTokenFindByTokenRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void shouldReturnMappedTokenWhenEntityExists(@Random String rawToken,
                                                 @Random OtpLoginEntity entity,
                                                 @Random OtpLoginToken expected) {
        var hashedToken = TokenUtil.sha256(rawToken);
        PanacheQuery<OtpLoginEntity> query = mock(PanacheQuery.class);
        given(panacheRepository.find("token", hashedToken)).willReturn(query);
        given(query.firstResultOptional()).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findByToken(rawToken);

        assertThat(result).isPresent().contains(expected);
        then(mapper).should().apply(entity);
    }

    @Test
    void shouldReturnEmptyWhenNoEntityFound(@Random String rawToken) {
        var hashedToken = TokenUtil.sha256(rawToken);
        PanacheQuery<OtpLoginEntity> query = mock(PanacheQuery.class);
        given(panacheRepository.find("token", hashedToken)).willReturn(query);
        given(query.firstResultOptional()).willReturn(Optional.empty());

        var result = sut.findByToken(rawToken);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void shouldHashRawTokenBeforeQuerying(@Random String rawToken) {
        var hashedToken = TokenUtil.sha256(rawToken);
        PanacheQuery<OtpLoginEntity> query = mock(PanacheQuery.class);
        given(panacheRepository.find("token", hashedToken)).willReturn(query);
        given(query.firstResultOptional()).willReturn(Optional.empty());

        sut.findByToken(rawToken);

        assertThat(hashedToken).isNotEqualTo(rawToken);
        then(panacheRepository).should().find("token", hashedToken);
    }
}
