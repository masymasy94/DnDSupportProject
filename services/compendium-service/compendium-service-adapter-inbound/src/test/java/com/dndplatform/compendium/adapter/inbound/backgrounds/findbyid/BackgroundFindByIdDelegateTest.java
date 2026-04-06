package com.dndplatform.compendium.adapter.inbound.backgrounds.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.backgrounds.findall.mapper.BackgroundViewModelMapper;
import com.dndplatform.compendium.domain.BackgroundFindByIdService;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
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
class BackgroundFindByIdDelegateTest {

    @Mock
    private BackgroundFindByIdService service;

    @Mock
    private BackgroundViewModelMapper mapper;

    private BackgroundFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new BackgroundFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Background background, @Random BackgroundViewModel expected) {
        given(service.findById(background.id())).willReturn(background);
        given(mapper.apply(background)).willReturn(expected);

        BackgroundViewModel result = sut.findById(background.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(background.id());
        then(mapper).should(inOrder).apply(background);
        inOrder.verifyNoMoreInteractions();
    }
}
