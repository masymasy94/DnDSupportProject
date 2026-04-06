package com.dndplatform.compendium.adapter.inbound.species.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.species.findall.mapper.SpeciesViewModelMapper;
import com.dndplatform.compendium.domain.SpeciesFindAllService;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
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
class SpeciesFindAllDelegateTest {

    @Mock
    private SpeciesFindAllService service;

    @Mock
    private SpeciesViewModelMapper mapper;

    private SpeciesFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SpeciesFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Species s1, @Random Species s2,
                                 @Random SpeciesViewModel vm1, @Random SpeciesViewModel vm2) {
        given(service.findAll()).willReturn(List.of(s1, s2));
        given(mapper.apply(s1)).willReturn(vm1);
        given(mapper.apply(s2)).willReturn(vm2);

        List<SpeciesViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(s1);
        then(mapper).should().apply(s2);
    }
}
