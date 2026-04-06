package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.RefreshTokenMapper;
import com.dndplatform.auth.domain.model.RefreshToken;
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
class RefreshTokenFindByTokenAndIdRepositoryJpaTest {

    @Mock
    private RefreshTokenMapper mapper;

    @Mock
    private RefreshTokenPanacheRepository panacheRepository;

    private RefreshTokenFindByTokenAndIdRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshTokenFindByTokenAndIdRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void shouldReturnMappedTokenWhenEntityExists(@Random String token,
                                                 @Random long userId,
                                                 @Random RefreshTokenEntity entity,
                                                 @Random RefreshToken expected) {
        PanacheQuery<RefreshTokenEntity> query = mock(PanacheQuery.class);
        given(panacheRepository.find("token = ?1 and userId = ?2", token, userId)).willReturn(query);
        given(query.firstResultOptional()).willReturn(Optional.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findByTokenAndId(token, userId);

        assertThat(result).isPresent().contains(expected);
        then(mapper).should().apply(entity);
    }

    @Test
    void shouldReturnEmptyWhenNoEntityFound(@Random String token, @Random long userId) {
        PanacheQuery<RefreshTokenEntity> query = mock(PanacheQuery.class);
        given(panacheRepository.find("token = ?1 and userId = ?2", token, userId)).willReturn(query);
        given(query.firstResultOptional()).willReturn(Optional.empty());

        var result = sut.findByTokenAndId(token, userId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
