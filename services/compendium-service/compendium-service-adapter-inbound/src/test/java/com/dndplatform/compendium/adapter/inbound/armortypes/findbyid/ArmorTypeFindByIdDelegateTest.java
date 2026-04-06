package com.dndplatform.compendium.adapter.inbound.armortypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.armortypes.findall.mapper.ArmorTypeViewModelMapper;
import com.dndplatform.compendium.domain.ArmorTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
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
class ArmorTypeFindByIdDelegateTest {

    @Mock
    private ArmorTypeFindByIdService service;

    @Mock
    private ArmorTypeViewModelMapper mapper;

    private ArmorTypeFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ArmorTypeFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random ArmorType armorType, @Random ArmorTypeViewModel expected) {
        given(service.findById(armorType.id())).willReturn(armorType);
        given(mapper.apply(armorType)).willReturn(expected);

        ArmorTypeViewModel result = sut.findById(armorType.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(armorType.id());
        then(mapper).should(inOrder).apply(armorType);
        inOrder.verifyNoMoreInteractions();
    }
}
