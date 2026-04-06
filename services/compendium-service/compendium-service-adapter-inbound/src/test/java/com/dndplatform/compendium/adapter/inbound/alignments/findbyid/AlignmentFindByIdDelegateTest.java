package com.dndplatform.compendium.adapter.inbound.alignments.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.alignments.findall.mapper.AlignmentViewModelMapper;
import com.dndplatform.compendium.domain.AlignmentFindByIdService;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
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
class AlignmentFindByIdDelegateTest {

    @Mock
    private AlignmentFindByIdService service;

    @Mock
    private AlignmentViewModelMapper mapper;

    private AlignmentFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new AlignmentFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Alignment alignment, @Random AlignmentViewModel expected) {
        given(service.findById(alignment.id())).willReturn(alignment);
        given(mapper.apply(alignment)).willReturn(expected);

        AlignmentViewModel result = sut.findById(alignment.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(alignment.id());
        then(mapper).should(inOrder).apply(alignment);
        inOrder.verifyNoMoreInteractions();
    }
}
