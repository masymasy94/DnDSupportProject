package com.dndplatform.compendium.adapter.inbound.monsters.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.monsters.findall.mapper.MonsterViewModelMapper;
import com.dndplatform.compendium.domain.MonsterFindByIdService;
import com.dndplatform.compendium.domain.model.Monster;
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
class MonsterFindByIdDelegateTest {

    @Mock
    private MonsterFindByIdService service;

    @Mock
    private MonsterViewModelMapper mapper;

    private MonsterFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new MonsterFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Monster monster, @Random MonsterViewModel expected) {
        given(service.findById(monster.id())).willReturn(monster);
        given(mapper.apply(monster)).willReturn(expected);

        MonsterViewModel result = sut.findById(monster.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(monster.id());
        then(mapper).should(inOrder).apply(monster);
        inOrder.verifyNoMoreInteractions();
    }
}
