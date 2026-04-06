package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.AlignmentFindAllService;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class AlignmentFindAllServiceImplTest {

    @Mock
    private AlignmentFindAllRepository repository;

    private AlignmentFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new AlignmentFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Alignment alignment) {
        List<Alignment> expected = List.of(alignment);
        given(repository.findAllAlignment()).willReturn(expected);

        List<Alignment> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllAlignment();
        inOrder.verifyNoMoreInteractions();
    }
}
