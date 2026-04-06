package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MonsterMapper;
import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MonsterFindAllRepositoryImplTest {

    @Mock private MonsterPanacheRepository panacheRepository;
    @Mock private MonsterMapper mapper;
    @Mock private Monster monster;
    private MonsterFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MonsterFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnPagedMonsters() {
        var criteria = new MonsterFilterCriteria(null, null, null, null, null, 0, 10);
        var entity = new MonsterEntity();
        var filter = QueryFilterUtils.create(criteria);
        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(1L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(10))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(monster);

        var result = sut.findAllMonsters(criteria);

        assertThat(result.content()).containsExactly(monster);
        assertThat(result.totalElements()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmptyPage() {
        var criteria = new MonsterFilterCriteria(null, null, null, null, null, 0, 10);
        var filter = QueryFilterUtils.create(criteria);
        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(0L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(10))).willReturn(List.of());

        var result = sut.findAllMonsters(criteria);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0L);
        then(mapper).shouldHaveNoInteractions();
    }
}
