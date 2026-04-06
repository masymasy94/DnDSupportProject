package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.SpellSchoolFindByIdService;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SpellSchoolFindByIdServiceImplTest {

    @Mock
    private SpellSchoolFindByIdRepository repository;

    private SpellSchoolFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new SpellSchoolFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindSpellSchoolById() {
        int id = 3;
        SpellSchool expected = new SpellSchool((short) id, "Evocation");

        given(repository.findById(id)).willReturn(Optional.of(expected));

        SpellSchool result = sut.findById(id);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSpellSchoolDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Spell school not found with id: " + id);

        then(repository).should().findById(id);
    }
}
