package com.dndplatform.compendium.adapter.inbound.tooltypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.tooltypes.findall.mapper.ToolTypeViewModelMapper;
import com.dndplatform.compendium.domain.ToolTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
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
class ToolTypeFindByIdDelegateTest {

    @Mock
    private ToolTypeFindByIdService service;

    @Mock
    private ToolTypeViewModelMapper mapper;

    private ToolTypeFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ToolTypeFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random ToolType toolType, @Random ToolTypeViewModel expected) {
        given(service.findById(toolType.id())).willReturn(toolType);
        given(mapper.apply(toolType)).willReturn(expected);

        ToolTypeViewModel result = sut.findById(toolType.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(toolType.id());
        then(mapper).should(inOrder).apply(toolType);
        inOrder.verifyNoMoreInteractions();
    }
}
