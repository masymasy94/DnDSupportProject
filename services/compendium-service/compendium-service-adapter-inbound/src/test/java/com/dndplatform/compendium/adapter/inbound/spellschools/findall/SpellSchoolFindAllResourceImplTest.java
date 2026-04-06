package com.dndplatform.compendium.adapter.inbound.spellschools.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.SpellSchoolFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpellSchoolFindAllResourceImplTest {
    @Mock private SpellSchoolFindAllResource delegate;
    private SpellSchoolFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new SpellSchoolFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random SpellSchoolViewModel vm1, @Random SpellSchoolViewModel vm2) {
        List<SpellSchoolViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<SpellSchoolViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}
