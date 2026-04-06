package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.ProficiencyTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ProficiencyTypeFindAllResourceImplTest {
    @Mock private ProficiencyTypeFindAllResource delegate;
    private ProficiencyTypeFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new ProficiencyTypeFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random ProficiencyTypeViewModel vm1, @Random ProficiencyTypeViewModel vm2) {
        List<ProficiencyTypeViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<ProficiencyTypeViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}
