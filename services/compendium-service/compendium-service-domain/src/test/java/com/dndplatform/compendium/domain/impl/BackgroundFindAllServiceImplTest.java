package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.BackgroundFindAllService;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindAllRepository;
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
class BackgroundFindAllServiceImplTest {

    @Mock
    private BackgroundFindAllRepository repository;

    private BackgroundFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new BackgroundFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Background background) {
        List<Background> expected = List.of(background);
        given(repository.findAllBackgrounds()).willReturn(expected);

        List<Background> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllBackgrounds();
        inOrder.verifyNoMoreInteractions();
    }
}
