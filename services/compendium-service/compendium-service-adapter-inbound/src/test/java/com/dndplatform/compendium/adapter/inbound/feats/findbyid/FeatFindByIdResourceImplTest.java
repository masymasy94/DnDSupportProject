package com.dndplatform.compendium.adapter.inbound.feats.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.FeatFindByIdResource;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class FeatFindByIdResourceImplTest {
    @Mock private FeatFindByIdResource delegate;
    private FeatFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new FeatFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random FeatViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        FeatViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
