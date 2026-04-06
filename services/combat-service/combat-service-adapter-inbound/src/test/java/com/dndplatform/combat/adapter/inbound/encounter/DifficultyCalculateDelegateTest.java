package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.DifficultyResultViewModelMapper;
import com.dndplatform.combat.domain.DifficultyCalculateService;
import com.dndplatform.combat.domain.model.DifficultyResult;
import com.dndplatform.combat.view.model.vm.DifficultyResultViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DifficultyCalculateDelegateTest {

    @Mock
    private DifficultyCalculateService service;

    @Mock
    private DifficultyResultViewModelMapper viewModelMapper;

    private DifficultyCalculateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DifficultyCalculateDelegate(service, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random Long encounterId,
                                 @Random DifficultyResult result,
                                 @Random DifficultyResultViewModel expected) {
        given(service.calculate(encounterId)).willReturn(result);
        given(viewModelMapper.apply(result)).willReturn(expected);

        var actual = sut.calculate(encounterId);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).calculate(encounterId);
        then(viewModelMapper).should(inOrder).apply(result);
    }
}
