package com.dndplatform.compendium.adapter.inbound.monsters.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.MonsterFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedMonsterViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MonsterFindAllResourceImplTest {
    @Mock private MonsterFindAllResource delegate;
    private MonsterFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new MonsterFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random PagedMonsterViewModel expected) {
        String name = "dragon";
        String type = "Dragon";
        String size = "Huge";
        String challengeRating = "17";
        String alignment = "chaotic evil";
        Integer page = 0;
        Integer pageSize = 50;
        given(delegate.findAll(name, type, size, challengeRating, alignment, page, pageSize)).willReturn(expected);
        PagedMonsterViewModel result = sut.findAll(name, type, size, challengeRating, alignment, page, pageSize);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(name, type, size, challengeRating, alignment, page, pageSize);
        inOrder.verifyNoMoreInteractions();
    }
}
