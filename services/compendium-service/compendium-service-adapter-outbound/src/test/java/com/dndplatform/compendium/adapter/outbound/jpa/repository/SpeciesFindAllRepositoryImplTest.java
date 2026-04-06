package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpeciesEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpeciesMapper;
import com.dndplatform.compendium.domain.model.Species;
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
class SpeciesFindAllRepositoryImplTest {

    @Mock
    private SpeciesPanacheRepository panacheRepository;

    @Mock
    private SpeciesMapper mapper;

    private SpeciesFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SpeciesFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedSpecies(@Random SpeciesEntity entity, @Random Species expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllSpecies();

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoSpeciesFound() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllSpecies();

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
