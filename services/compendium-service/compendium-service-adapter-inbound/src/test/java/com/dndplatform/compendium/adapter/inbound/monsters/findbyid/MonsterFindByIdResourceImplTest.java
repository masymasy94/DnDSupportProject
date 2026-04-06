package com.dndplatform.compendium.adapter.inbound.monsters.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.MonsterFindByIdResource;
import com.dndplatform.compendium.view.model.vm.MonsterViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MonsterFindByIdResourceImplTest {

    @Mock
    private MonsterFindByIdResource delegate;

    private MonsterFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MonsterFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateToDelegate(@Random MonsterViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);

        MonsterViewModel result = sut.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
