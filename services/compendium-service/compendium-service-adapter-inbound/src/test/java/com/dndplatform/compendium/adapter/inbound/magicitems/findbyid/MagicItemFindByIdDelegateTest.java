package com.dndplatform.compendium.adapter.inbound.magicitems.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.magicitems.findall.mapper.MagicItemViewModelMapper;
import com.dndplatform.compendium.domain.MagicItemFindByIdService;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
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
class MagicItemFindByIdDelegateTest {

    @Mock
    private MagicItemFindByIdService service;

    @Mock
    private MagicItemViewModelMapper mapper;

    private MagicItemFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new MagicItemFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random MagicItem magicItem, @Random MagicItemViewModel expected) {
        given(service.findById(magicItem.id())).willReturn(magicItem);
        given(mapper.apply(magicItem)).willReturn(expected);

        MagicItemViewModel result = sut.findById(magicItem.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(magicItem.id());
        then(mapper).should(inOrder).apply(magicItem);
        inOrder.verifyNoMoreInteractions();
    }
}
