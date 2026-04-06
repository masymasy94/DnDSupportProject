package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.adapter.inbound.mapper.UpdateEncounterMapper;
import com.dndplatform.combat.domain.EncounterUpdateService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.combat.domain.model.EncounterUpdateBuilder;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterUpdateDelegateTest {

    @Mock
    private EncounterUpdateService service;

    @Mock
    private UpdateEncounterMapper updateMapper;

    @Mock
    private EncounterViewModelMapper viewModelMapper;

    private EncounterUpdateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterUpdateDelegate(service, updateMapper, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random Long id,
                                 @Random UpdateEncounterRequest request,
                                 @Random EncounterUpdate baseUpdate,
                                 @Random Encounter encounter,
                                 @Random EncounterViewModel expected) {
        given(updateMapper.apply(request)).willReturn(baseUpdate);
        given(service.update(org.mockito.ArgumentMatchers.eq(request.userId()), org.mockito.ArgumentMatchers.any(EncounterUpdate.class))).willReturn(encounter);
        given(viewModelMapper.apply(encounter)).willReturn(expected);

        var actual = sut.update(id, request);

        assertThat(actual).isEqualTo(expected);

        ArgumentCaptor<EncounterUpdate> captor = ArgumentCaptor.forClass(EncounterUpdate.class);
        then(service).should().update(org.mockito.ArgumentMatchers.eq(request.userId()), captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        then(viewModelMapper).should().apply(encounter);
    }
}
