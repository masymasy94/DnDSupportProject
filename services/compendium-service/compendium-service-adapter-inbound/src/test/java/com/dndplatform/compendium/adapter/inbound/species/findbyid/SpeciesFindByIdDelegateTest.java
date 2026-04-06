package com.dndplatform.compendium.adapter.inbound.species.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.SpeciesFindByIdService;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpeciesFindByIdDelegateTest {

    @Mock
    private SpeciesFindByIdService service;

    private SpeciesFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SpeciesFindByIdDelegate(service);
    }

    @Test
    void shouldDelegateToService(@Random Species species) {
        given(service.findById(species.id())).willReturn(Optional.of(species));

        SpeciesViewModel result = sut.findById(species.id());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(species.id());
        assertThat(result.name()).isEqualTo(species.name());
        assertThat(result.description()).isEqualTo(species.description());
        assertThat(result.source()).isEqualTo(species.source().name());
        assertThat(result.isPublic()).isEqualTo(species.isPublic());

        var inOrder = inOrder(service);
        then(service).should(inOrder).findById(species.id());
        inOrder.verifyNoMoreInteractions();
    }
}
