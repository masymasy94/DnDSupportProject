package com.dndplatform.compendium.adapter.inbound.monsters.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.monsters.findall.mapper.MonsterViewModelMapper;
import com.dndplatform.compendium.domain.MonsterFindAllService;
import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.view.model.vm.MonsterViewModel;
import com.dndplatform.compendium.view.model.vm.PagedMonsterViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MonsterFindAllDelegateTest {

    @Mock
    private MonsterFindAllService service;

    @Mock
    private MonsterViewModelMapper mapper;

    private MonsterFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new MonsterFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Monster m1, @Random Monster m2,
                                 @Random MonsterViewModel vm1, @Random MonsterViewModel vm2) {
        var pagedResult = new PagedResult<>(List.of(m1, m2), 0, 50, 2L);
        given(mapper.apply(m1)).willReturn(vm1);
        given(mapper.apply(m2)).willReturn(vm2);

        ArgumentCaptor<MonsterFilterCriteria> criteriaCaptor = ArgumentCaptor.forClass(MonsterFilterCriteria.class);
        given(service.findAll(criteriaCaptor.capture())).willReturn(pagedResult);

        PagedMonsterViewModel result = sut.findAll("dragon", "beast", "large", "5", "chaotic evil", 0, 50);

        assertThat(result.content()).containsExactly(vm1, vm2);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(50);
        assertThat(result.totalElements()).isEqualTo(2L);

        MonsterFilterCriteria criteria = criteriaCaptor.getValue();
        assertThat(criteria.name()).isEqualTo("dragon");
        assertThat(criteria.type()).isEqualTo("beast");
        assertThat(criteria.size()).isEqualTo("large");
        assertThat(criteria.challengeRating()).isEqualTo("5");
        assertThat(criteria.alignment()).isEqualTo("chaotic evil");
        assertThat(criteria.page()).isEqualTo(0);
        assertThat(criteria.pageSize()).isEqualTo(50);

        then(service).should().findAll(criteria);
        then(mapper).should().apply(m1);
        then(mapper).should().apply(m2);
    }
}
