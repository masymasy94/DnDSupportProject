package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.BackgroundEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.BackgroundMapper;
import com.dndplatform.compendium.domain.model.Background;
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
class BackgroundFindAllRepositoryImplTest {

    @Mock
    private BackgroundPanacheRepository panacheRepository;

    @Mock
    private BackgroundMapper mapper;

    private BackgroundFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new BackgroundFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedBackground(@Random BackgroundEntity entity, @Random Background expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllBackgrounds();

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoBackgroundFound() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllBackgrounds();

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
