package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.ProficiencyTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ProficiencyTypeFindByIdResourceImplTest {
    @Mock private ProficiencyTypeFindByIdResource delegate;
    private ProficiencyTypeFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new ProficiencyTypeFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random ProficiencyTypeViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        ProficiencyTypeViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
