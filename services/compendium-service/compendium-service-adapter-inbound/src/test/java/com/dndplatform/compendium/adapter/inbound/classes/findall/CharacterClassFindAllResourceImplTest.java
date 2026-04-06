package com.dndplatform.compendium.adapter.inbound.classes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.CharacterClassFindAllResource;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterClassFindAllResourceImplTest {
    @Mock private CharacterClassFindAllResource delegate;
    private CharacterClassFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new CharacterClassFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random CharacterClassViewModel vm1, @Random CharacterClassViewModel vm2) {
        List<CharacterClassViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<CharacterClassViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}
