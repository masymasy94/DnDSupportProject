package com.dndplatform.compendium.adapter.inbound.damagetypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.damagetypes.findall.mapper.DamageTypeViewModelMapper;
import com.dndplatform.compendium.domain.DamageTypeFindByIdService;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
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
class DamageTypeFindByIdDelegateTest {

    @Mock
    private DamageTypeFindByIdService service;

    @Mock
    private DamageTypeViewModelMapper mapper;

    private DamageTypeFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DamageTypeFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random DamageType damageType, @Random DamageTypeViewModel expected) {
        given(service.findById(damageType.id())).willReturn(damageType);
        given(mapper.apply(damageType)).willReturn(expected);

        DamageTypeViewModel result = sut.findById(damageType.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(damageType.id());
        then(mapper).should(inOrder).apply(damageType);
        inOrder.verifyNoMoreInteractions();
    }
}
