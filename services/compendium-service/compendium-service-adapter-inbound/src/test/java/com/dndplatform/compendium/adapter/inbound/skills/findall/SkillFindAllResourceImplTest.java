package com.dndplatform.compendium.adapter.inbound.skills.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.SkillFindAllResource;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SkillFindAllResourceImplTest {
    @Mock private SkillFindAllResource delegate;
    private SkillFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new SkillFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random SkillViewModel vm1, @Random SkillViewModel vm2) {
        List<SkillViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<SkillViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}
