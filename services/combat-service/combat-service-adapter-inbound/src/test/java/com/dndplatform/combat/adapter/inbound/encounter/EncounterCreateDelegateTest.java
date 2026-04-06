package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.CreateEncounterMapper;
import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterCreateService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
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
class EncounterCreateDelegateTest {

    @Mock
    private EncounterCreateService service;

    @Mock
    private CreateEncounterMapper createMapper;

    @Mock
    private EncounterViewModelMapper viewModelMapper;

    private EncounterCreateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterCreateDelegate(service, createMapper, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random CreateEncounterRequest request,
                                 @Random EncounterCreate domainCreate,
                                 @Random Encounter encounter,
                                 @Random EncounterViewModel expected) {
        given(createMapper.apply(request)).willReturn(domainCreate);
        given(service.create(domainCreate)).willReturn(encounter);
        given(viewModelMapper.apply(encounter)).willReturn(expected);

        var actual = sut.create(request);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(createMapper, service, viewModelMapper);
        then(createMapper).should(inOrder).apply(request);
        then(service).should(inOrder).create(domainCreate);
        then(viewModelMapper).should(inOrder).apply(encounter);
    }
}
