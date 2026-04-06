package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.FeatEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.FeatMapper;
import com.dndplatform.compendium.domain.model.Feat;
import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class FeatFindAllRepositoryImplTest {

    @Mock
    private FeatPanacheRepository panacheRepository;

    @Mock
    private FeatMapper mapper;

    private FeatFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new FeatFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedFeats(@Random FeatEntity entity, @Random Feat expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        assertThat(sut.findAllFeats()).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyList() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        assertThat(sut.findAllFeats()).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
