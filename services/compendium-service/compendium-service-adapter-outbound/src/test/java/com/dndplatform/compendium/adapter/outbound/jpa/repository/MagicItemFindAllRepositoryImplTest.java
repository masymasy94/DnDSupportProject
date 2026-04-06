package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MagicItemMapper;
import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
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
class MagicItemFindAllRepositoryImplTest {

    @Mock private MagicItemPanacheRepository panacheRepository;
    @Mock private MagicItemMapper mapper;
    @Mock private MagicItem magicItem;
    private MagicItemFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MagicItemFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnPagedMagicItems() {
        var criteria = new MagicItemFilterCriteria(null, null, null, null, 0, 10);
        var entity = new MagicItemEntity();
        var filter = QueryFilterUtils.create(criteria);
        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(1L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(10))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(magicItem);

        var result = sut.findAllMagicItems(criteria);

        assertThat(result.content()).containsExactly(magicItem);
        assertThat(result.totalElements()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmptyPage() {
        var criteria = new MagicItemFilterCriteria(null, null, null, null, 0, 10);
        var filter = QueryFilterUtils.create(criteria);
        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(0L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(10))).willReturn(List.of());

        var result = sut.findAllMagicItems(criteria);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0L);
        then(mapper).shouldHaveNoInteractions();
    }
}
