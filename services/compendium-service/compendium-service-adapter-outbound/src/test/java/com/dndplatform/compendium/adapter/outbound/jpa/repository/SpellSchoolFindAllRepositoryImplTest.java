package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellSchoolEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellSchoolMapper;
import com.dndplatform.compendium.domain.model.SpellSchool;
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
class SpellSchoolFindAllRepositoryImplTest {

    @Mock
    private SpellSchoolPanacheRepository panacheRepository;

    @Mock
    private SpellSchoolMapper mapper;

    private SpellSchoolFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SpellSchoolFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedSpellSchool(@Random SpellSchoolEntity entity, @Random SpellSchool expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        var result = sut.findAllSpellSchools();

        assertThat(result).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyListWhenNoSpellSchoolFound() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        var result = sut.findAllSpellSchools();

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
