package com.dndplatform.compendium.adapter.inbound.magicitems.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.MagicItemFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedMagicItemViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MagicItemFindAllResourceImplTest {
    @Mock private MagicItemFindAllResource delegate;
    private MagicItemFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new MagicItemFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random PagedMagicItemViewModel expected) {
        String name = "ring";
        String rarity = "Rare";
        String type = "Ring";
        Boolean requiresAttunement = true;
        Integer page = 0;
        Integer pageSize = 50;
        given(delegate.findAll(name, rarity, type, requiresAttunement, page, pageSize)).willReturn(expected);
        PagedMagicItemViewModel result = sut.findAll(name, rarity, type, requiresAttunement, page, pageSize);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(name, rarity, type, requiresAttunement, page, pageSize);
        inOrder.verifyNoMoreInteractions();
    }
}
