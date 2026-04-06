package com.dndplatform.compendium.adapter.inbound.spells.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.spells.findall.mapper.SpellViewModelMapper;
import com.dndplatform.compendium.domain.SpellFindAllService;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.view.model.vm.PagedSpellViewModel;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
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
class SpellFindAllDelegateTest {

    @Mock
    private SpellFindAllService service;

    @Mock
    private SpellViewModelMapper mapper;

    private SpellFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SpellFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Spell s1, @Random Spell s2,
                                 @Random SpellViewModel vm1, @Random SpellViewModel vm2) {
        var pagedResult = new PagedResult<>(List.of(s1, s2), 0, 50, 2L);
        given(mapper.apply(s1)).willReturn(vm1);
        given(mapper.apply(s2)).willReturn(vm2);

        ArgumentCaptor<SpellFilterCriteria> criteriaCaptor = ArgumentCaptor.forClass(SpellFilterCriteria.class);
        given(service.findAll(criteriaCaptor.capture())).willReturn(pagedResult);

        PagedSpellViewModel result = sut.findAll("fireball", List.of(3), List.of("Evocation"), true, false, 0, 50);

        assertThat(result.content()).containsExactly(vm1, vm2);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(50);
        assertThat(result.totalElements()).isEqualTo(2L);

        SpellFilterCriteria criteria = criteriaCaptor.getValue();
        assertThat(criteria.search()).isEqualTo("fireball");
        assertThat(criteria.levels()).containsExactly(3);
        assertThat(criteria.schools()).containsExactly("Evocation");
        assertThat(criteria.concentration()).isTrue();
        assertThat(criteria.ritual()).isFalse();
        assertThat(criteria.page()).isEqualTo(0);
        assertThat(criteria.pageSize()).isEqualTo(50);

        then(service).should().findAll(criteria);
        then(mapper).should().apply(s1);
        then(mapper).should().apply(s2);
    }
}
