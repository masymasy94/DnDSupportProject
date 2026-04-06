package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.SpeciesFindAllService;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindAllRepository;
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
class SpeciesFindAllServiceImplTest {

    @Mock
    private SpeciesFindAllRepository repository;

    private SpeciesFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new SpeciesFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Species species) {
        List<Species> expected = List.of(species);
        given(repository.findAllSpecies()).willReturn(expected);

        List<Species> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllSpecies();
        inOrder.verifyNoMoreInteractions();
    }
}
