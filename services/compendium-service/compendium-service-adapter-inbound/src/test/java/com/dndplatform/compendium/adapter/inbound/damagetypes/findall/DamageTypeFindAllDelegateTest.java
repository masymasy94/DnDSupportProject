package com.dndplatform.compendium.adapter.inbound.damagetypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.damagetypes.findall.mapper.DamageTypeViewModelMapper;
import com.dndplatform.compendium.domain.DamageTypeFindAllService;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DamageTypeFindAllDelegateTest {

    @Mock
    private DamageTypeFindAllService service;

    @Mock
    private DamageTypeViewModelMapper mapper;

    private DamageTypeFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DamageTypeFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random DamageType d1, @Random DamageType d2,
                                 @Random DamageTypeViewModel vm1, @Random DamageTypeViewModel vm2) {
        given(service.findAll()).willReturn(List.of(d1, d2));
        given(mapper.apply(d1)).willReturn(vm1);
        given(mapper.apply(d2)).willReturn(vm2);

        List<DamageTypeViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(d1);
        then(mapper).should().apply(d2);
    }
}
