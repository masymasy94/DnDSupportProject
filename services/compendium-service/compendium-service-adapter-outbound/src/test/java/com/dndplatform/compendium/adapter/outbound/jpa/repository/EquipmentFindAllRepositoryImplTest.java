package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.EquipmentMapper;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
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
class EquipmentFindAllRepositoryImplTest {

    @Mock
    private EquipmentPanacheRepository panacheRepository;

    @Mock
    private EquipmentMapper mapper;

    @Mock
    private Equipment equipment;

    private EquipmentFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EquipmentFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnPagedEquipment() {
        var criteria = new EquipmentFilterCriteria(null, null, 0, 10);
        var entity = new EquipmentEntity();
        var filter = QueryFilterUtils.create(criteria);

        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(1L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(10))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(equipment);

        var result = sut.findAllEquipment(criteria);

        assertThat(result.content()).containsExactly(equipment);
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    void shouldReturnEmptyPageWhenNoResults() {
        var criteria = new EquipmentFilterCriteria(null, null, 0, 10);
        var filter = QueryFilterUtils.create(criteria);

        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(0L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(10))).willReturn(List.of());

        var result = sut.findAllEquipment(criteria);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0L);
        then(mapper).shouldHaveNoInteractions();
    }
}
