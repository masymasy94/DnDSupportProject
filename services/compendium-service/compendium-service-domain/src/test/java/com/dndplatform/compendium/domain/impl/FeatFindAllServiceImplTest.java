package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.FeatFindAllService;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.domain.repository.FeatFindAllRepository;
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
class FeatFindAllServiceImplTest {

    @Mock
    private FeatFindAllRepository repository;

    private FeatFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new FeatFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Feat feat) {
        List<Feat> expected = List.of(feat);
        given(repository.findAllFeats()).willReturn(expected);

        List<Feat> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllFeats();
        inOrder.verifyNoMoreInteractions();
    }
}
