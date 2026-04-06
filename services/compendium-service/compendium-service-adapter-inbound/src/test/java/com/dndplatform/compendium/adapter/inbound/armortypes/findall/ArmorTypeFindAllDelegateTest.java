package com.dndplatform.compendium.adapter.inbound.armortypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.armortypes.findall.mapper.ArmorTypeViewModelMapper;
import com.dndplatform.compendium.domain.ArmorTypeFindAllService;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
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
class ArmorTypeFindAllDelegateTest {

    @Mock
    private ArmorTypeFindAllService service;

    @Mock
    private ArmorTypeViewModelMapper mapper;

    private ArmorTypeFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ArmorTypeFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random ArmorType a1, @Random ArmorType a2,
                                 @Random ArmorTypeViewModel vm1, @Random ArmorTypeViewModel vm2) {
        given(service.findAll()).willReturn(List.of(a1, a2));
        given(mapper.apply(a1)).willReturn(vm1);
        given(mapper.apply(a2)).willReturn(vm2);

        List<ArmorTypeViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(a1);
        then(mapper).should().apply(a2);
    }
}
