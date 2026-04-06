package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.ConditionFindAllService;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindAllRepository;
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
class ConditionFindAllServiceImplTest {

    @Mock
    private ConditionFindAllRepository repository;

    private ConditionFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new ConditionFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Condition condition) {
        List<Condition> expected = List.of(condition);
        given(repository.findAllConditions()).willReturn(expected);

        List<Condition> result = sut.findAll();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllConditions();
        inOrder.verifyNoMoreInteractions();
    }
}
