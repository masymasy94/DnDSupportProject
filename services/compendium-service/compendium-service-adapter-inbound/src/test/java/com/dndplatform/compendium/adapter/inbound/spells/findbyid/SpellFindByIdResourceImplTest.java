package com.dndplatform.compendium.adapter.inbound.spells.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.SpellFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpellFindByIdResourceImplTest {
    @Mock private SpellFindByIdResource delegate;
    private SpellFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new SpellFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random SpellViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        SpellViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
