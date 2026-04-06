package com.dndplatform.compendium.adapter.inbound.spells.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.spells.findall.mapper.SpellViewModelMapper;
import com.dndplatform.compendium.domain.SpellFindByIdService;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpellFindByIdDelegateTest {

    @Mock
    private SpellFindByIdService service;

    @Mock
    private SpellViewModelMapper mapper;

    private SpellFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SpellFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Spell spell, @Random SpellViewModel expected) {
        given(service.findById(spell.id())).willReturn(spell);
        given(mapper.apply(spell)).willReturn(expected);

        SpellViewModel result = sut.findById(spell.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(spell.id());
        then(mapper).should(inOrder).apply(spell);
        inOrder.verifyNoMoreInteractions();
    }
}
