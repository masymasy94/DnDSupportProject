package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.SpellFindAllService;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.model.SourceType;
import com.dndplatform.compendium.domain.repository.SpellFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith(MockitoExtension.class)
class SpellFindAllServiceImplTest {

    @Mock
    private SpellFindAllRepository repository;

    private SpellFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new SpellFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAllSpellsWithCriteria() {
        SpellFilterCriteria criteria = new SpellFilterCriteria(null, null, null, null, null, 0, 20);
        List<Spell> spells = List.of(createSpell(1), createSpell(2));
        PagedResult<Spell> expected = new PagedResult<>(spells, 0, 20, 2);

        given(repository.findAllSpells(criteria)).willReturn(expected);

        PagedResult<Spell> result = sut.findAll(criteria);

        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(2);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllSpells(criteria);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyPageWhenNoSpells() {
        SpellFilterCriteria criteria = new SpellFilterCriteria(null, null, null, null, null, 0, 20);
        PagedResult<Spell> expected = new PagedResult<>(List.of(), 0, 20, 0);

        given(repository.findAllSpells(criteria)).willReturn(expected);

        PagedResult<Spell> result = sut.findAll(criteria);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();

        then(repository).should().findAllSpells(criteria);
    }

    private Spell createSpell(int id) {
        return new Spell(
                id,
                "Spell " + id,
                1,
                "Evocation",
                "1 action",
                "60 feet",
                "V, S",
                "a drop of mercury",
                "Instantaneous",
                false,
                false,
                "Description of spell " + id,
                "At higher levels: +1d6",
                SourceType.OFFICIAL,
                1L,
                1L,
                true
        );
    }
}
