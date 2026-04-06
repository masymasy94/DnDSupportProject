package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterFindByIdService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterFindByIdDelegateTest {

    @Mock
    private EncounterFindByIdService service;

    @Mock
    private EncounterViewModelMapper viewModelMapper;

    private EncounterFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByIdDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnMappedEncounter(@Random Long id,
                                     @Random Encounter encounter,
                                     @Random EncounterViewModel expected) {
        given(service.findById(id)).willReturn(encounter);
        given(viewModelMapper.apply(encounter)).willReturn(expected);

        var actual = sut.findById(id);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findById(id);
        then(viewModelMapper).should(inOrder).apply(encounter);
    }

    @Test
    void shouldThrowWhenEncounterNotFound(@Random Long id) {
        willThrow(new NotFoundException("Encounter not found with id: " + id))
                .given(service).findById(id);

        assertThatThrownBy(() -> sut.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.valueOf(id));
    }
}
