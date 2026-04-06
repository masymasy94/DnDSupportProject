package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellMapper;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.Spell;
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
class SpellFindAllRepositoryImplTest {

    @Mock
    private SpellPanacheRepository panacheRepository;

    @Mock
    private SpellMapper mapper;

    @Mock
    private Spell spell;

    private SpellFindAllRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SpellFindAllRepositoryImpl(panacheRepository, mapper);
    }

    @Test
    void shouldReturnPagedSpells() {
        var criteria = new SpellFilterCriteria(null, null, null, null, null, 0, 20);
        var entity = new SpellEntity();
        var filter = QueryFilterUtils.create(criteria);
        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(1L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(20))).willReturn(List.of(entity));
        given(mapper.apply(entity)).willReturn(spell);

        var result = sut.findAllSpells(criteria);

        assertThat(result.content()).containsExactly(spell);
        assertThat(result.totalElements()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmptyPage() {
        var criteria = new SpellFilterCriteria(null, null, null, null, null, 0, 20);
        var filter = QueryFilterUtils.create(criteria);
        given(panacheRepository.countFiltered(eq(filter.query()), eq(filter.params()))).willReturn(0L);
        given(panacheRepository.findFiltered(eq(filter.query()), eq(filter.params()), any(Sort.class), eq(0), eq(20))).willReturn(List.of());

        var result = sut.findAllSpells(criteria);

        assertThat(result.content()).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
