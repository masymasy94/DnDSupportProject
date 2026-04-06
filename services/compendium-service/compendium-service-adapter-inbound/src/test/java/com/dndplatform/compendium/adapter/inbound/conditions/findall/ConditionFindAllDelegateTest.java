package com.dndplatform.compendium.adapter.inbound.conditions.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.conditions.findall.mapper.ConditionViewModelMapper;
import com.dndplatform.compendium.domain.ConditionFindAllService;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
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
class ConditionFindAllDelegateTest {

    @Mock
    private ConditionFindAllService service;

    @Mock
    private ConditionViewModelMapper mapper;

    private ConditionFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConditionFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Condition c1, @Random Condition c2,
                                 @Random ConditionViewModel vm1, @Random ConditionViewModel vm2) {
        given(service.findAll()).willReturn(List.of(c1, c2));
        given(mapper.apply(c1)).willReturn(vm1);
        given(mapper.apply(c2)).willReturn(vm2);

        List<ConditionViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(c1);
        then(mapper).should().apply(c2);
    }
}
