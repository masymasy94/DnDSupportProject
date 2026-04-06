package com.dndplatform.compendium.adapter.inbound.alignments.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.alignments.findall.mapper.AlignmentViewModelMapper;
import com.dndplatform.compendium.domain.AlignmentFindAllService;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
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
class AlignmentFindAllDelegateTest {

    @Mock
    private AlignmentFindAllService service;

    @Mock
    private AlignmentViewModelMapper mapper;

    private AlignmentFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new AlignmentFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Alignment a1, @Random Alignment a2,
                                 @Random AlignmentViewModel vm1, @Random AlignmentViewModel vm2) {
        given(service.findAll()).willReturn(List.of(a1, a2));
        given(mapper.apply(a1)).willReturn(vm1);
        given(mapper.apply(a2)).willReturn(vm2);

        List<AlignmentViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(a1);
        then(mapper).should().apply(a2);
    }
}
