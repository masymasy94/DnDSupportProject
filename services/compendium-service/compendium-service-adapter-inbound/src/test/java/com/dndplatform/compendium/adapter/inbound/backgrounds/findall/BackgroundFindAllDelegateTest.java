package com.dndplatform.compendium.adapter.inbound.backgrounds.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.backgrounds.findall.mapper.BackgroundViewModelMapper;
import com.dndplatform.compendium.domain.BackgroundFindAllService;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
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
class BackgroundFindAllDelegateTest {

    @Mock
    private BackgroundFindAllService service;

    @Mock
    private BackgroundViewModelMapper mapper;

    private BackgroundFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new BackgroundFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Background b1, @Random Background b2,
                                 @Random BackgroundViewModel vm1, @Random BackgroundViewModel vm2) {
        given(service.findAll()).willReturn(List.of(b1, b2));
        given(mapper.apply(b1)).willReturn(vm1);
        given(mapper.apply(b2)).willReturn(vm2);

        List<BackgroundViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(b1);
        then(mapper).should().apply(b2);
    }
}
