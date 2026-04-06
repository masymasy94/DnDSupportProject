package com.dndplatform.compendium.adapter.inbound.spells.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.SpellFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedSpellViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpellFindAllResourceImplTest {
    @Mock private SpellFindAllResource delegate;
    private SpellFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new SpellFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random PagedSpellViewModel expected) {
        String search = "fireball";
        List<Integer> levels = List.of(3);
        List<String> schools = List.of("Evocation");
        Boolean concentration = false;
        Boolean ritual = false;
        Integer page = 0;
        Integer pageSize = 50;
        given(delegate.findAll(search, levels, schools, concentration, ritual, page, pageSize)).willReturn(expected);
        PagedSpellViewModel result = sut.findAll(search, levels, schools, concentration, ritual, page, pageSize);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(search, levels, schools, concentration, ritual, page, pageSize);
        inOrder.verifyNoMoreInteractions();
    }
}
