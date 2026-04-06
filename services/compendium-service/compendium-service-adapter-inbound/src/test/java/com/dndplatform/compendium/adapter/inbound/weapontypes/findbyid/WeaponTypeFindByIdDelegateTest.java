package com.dndplatform.compendium.adapter.inbound.weapontypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.weapontypes.findall.mapper.WeaponTypeViewModelMapper;
import com.dndplatform.compendium.domain.WeaponTypeFindByIdService;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
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
class WeaponTypeFindByIdDelegateTest {

    @Mock
    private WeaponTypeFindByIdService service;

    @Mock
    private WeaponTypeViewModelMapper mapper;

    private WeaponTypeFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new WeaponTypeFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random WeaponType weaponType, @Random WeaponTypeViewModel expected) {
        given(service.findById(weaponType.id())).willReturn(weaponType);
        given(mapper.apply(weaponType)).willReturn(expected);

        WeaponTypeViewModel result = sut.findById(weaponType.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(weaponType.id());
        then(mapper).should(inOrder).apply(weaponType);
        inOrder.verifyNoMoreInteractions();
    }
}
