package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.SpellFindByIdService;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.SpellFindByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SpellFindByIdServiceImplTest {

    @Mock
    private SpellFindByIdRepository repository;

    private SpellFindByIdService sut;

    @BeforeEach
    void setUp() {
        sut = new SpellFindByIdServiceImpl(repository);
    }

    @Test
    void shouldFindSpellById() {
        int id = 1;
        Spell expected = new Spell(
                id,
                "Fireball",
                3,
                "Evocation",
                "1 action",
                "150 feet",
                "V, S, M",
                "A tiny ball of bat guano and sulfur",
                "Instantaneous",
                false,
                false,
                "A bright streak flashes from your pointing finger, then blossoms with a low roar into an explosion of flame.",
                "When you cast this spell using a spell slot of 4th level or higher, the damage increases by 1d6 for each slot level above 3rd.",
                SourceType.OFFICIAL,
                1L,
                1L,
                true
        );

        given(repository.findById(id)).willReturn(Optional.of(expected));

        Spell result = sut.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSpellDoesNotExist() {
        int id = 999;

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Spell not found with id: " + id);

        then(repository).should().findById(id);
    }
}