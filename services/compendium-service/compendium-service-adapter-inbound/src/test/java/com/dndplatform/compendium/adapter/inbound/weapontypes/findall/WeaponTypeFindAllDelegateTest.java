package com.dndplatform.compendium.adapter.inbound.weapontypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.weapontypes.findall.mapper.WeaponTypeViewModelMapper;
import com.dndplatform.compendium.domain.WeaponTypeFindAllService;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
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
class WeaponTypeFindAllDelegateTest {

    @Mock
    private WeaponTypeFindAllService service;

    @Mock
    private WeaponTypeViewModelMapper mapper;

    private WeaponTypeFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new WeaponTypeFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random WeaponType wt1, @Random WeaponType wt2,
                                 @Random WeaponTypeViewModel vm1, @Random WeaponTypeViewModel vm2) {
        given(service.findAll("simple")).willReturn(List.of(wt1, wt2));
        given(mapper.apply(wt1)).willReturn(vm1);
        given(mapper.apply(wt2)).willReturn(vm2);

        List<WeaponTypeViewModel> result = sut.findAll("simple");

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll("simple");
        then(mapper).should().apply(wt1);
        then(mapper).should().apply(wt2);
    }
}
