package com.dndplatform.compendium.adapter.inbound.spellschools.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.SpellSchoolFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpellSchoolFindByIdResourceImplTest {
    @Mock private SpellSchoolFindByIdResource delegate;
    private SpellSchoolFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new SpellSchoolFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random SpellSchoolViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        SpellSchoolViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
