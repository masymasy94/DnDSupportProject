package com.dndplatform.compendium.adapter.inbound.tooltypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.tooltypes.findall.mapper.ToolTypeViewModelMapper;
import com.dndplatform.compendium.domain.ToolTypeFindAllService;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
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
class ToolTypeFindAllDelegateTest {

    @Mock
    private ToolTypeFindAllService service;

    @Mock
    private ToolTypeViewModelMapper mapper;

    private ToolTypeFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ToolTypeFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random ToolType tt1, @Random ToolType tt2,
                                 @Random ToolTypeViewModel vm1, @Random ToolTypeViewModel vm2) {
        given(service.findAll("artisan")).willReturn(List.of(tt1, tt2));
        given(mapper.apply(tt1)).willReturn(vm1);
        given(mapper.apply(tt2)).willReturn(vm2);

        List<ToolTypeViewModel> result = sut.findAll("artisan");

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll("artisan");
        then(mapper).should().apply(tt1);
        then(mapper).should().apply(tt2);
    }
}
