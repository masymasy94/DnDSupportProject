package com.dndplatform.compendium.adapter.inbound.classes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.CharacterClassFindByIdResource;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterClassFindByIdResourceImplTest {
    @Mock private CharacterClassFindByIdResource delegate;
    private CharacterClassFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new CharacterClassFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random CharacterClassViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        CharacterClassViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
