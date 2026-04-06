package com.dndplatform.compendium.adapter.inbound.conditions.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.conditions.findall.mapper.ConditionViewModelMapper;
import com.dndplatform.compendium.domain.ConditionFindByIdService;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
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
class ConditionFindByIdDelegateTest {

    @Mock
    private ConditionFindByIdService service;

    @Mock
    private ConditionViewModelMapper mapper;

    private ConditionFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ConditionFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Condition condition, @Random ConditionViewModel expected) {
        given(service.findById(condition.id())).willReturn(condition);
        given(mapper.apply(condition)).willReturn(expected);

        ConditionViewModel result = sut.findById(condition.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(condition.id());
        then(mapper).should(inOrder).apply(condition);
        inOrder.verifyNoMoreInteractions();
    }
}
