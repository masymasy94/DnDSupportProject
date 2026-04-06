package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ConditionEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ConditionMapper;
import com.dndplatform.compendium.domain.model.Condition;
import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConditionFindAllRepositoryImplTest {

    @Mock private ConditionPanacheRepository panacheRepository;
    @Mock private ConditionMapper mapper;
    private ConditionFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ConditionFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedCondition(@Random ConditionEntity entity, @Random Condition expected) {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(expected);

        assertThat(sut.findAllConditions()).containsExactly(expected);
    }

    @Test
    void shouldReturnEmptyList() {
        given(panacheRepository.listAll(any(Sort.class))).willReturn(List.of());

        assertThat(sut.findAllConditions()).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
