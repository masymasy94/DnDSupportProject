package com.dndplatform.compendium.adapter.inbound.magicitems.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.magicitems.findall.mapper.MagicItemViewModelMapper;
import com.dndplatform.compendium.domain.MagicItemFindAllService;
import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
import com.dndplatform.compendium.view.model.vm.PagedMagicItemViewModel;
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
class MagicItemFindAllDelegateTest {

    @Mock
    private MagicItemFindAllService service;

    @Mock
    private MagicItemViewModelMapper mapper;

    private MagicItemFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new MagicItemFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random MagicItem item1, @Random MagicItem item2,
                                 @Random MagicItemViewModel vm1, @Random MagicItemViewModel vm2) {
        var pagedResult = new PagedResult<>(List.of(item1, item2), 0, 50, 2L);
        given(mapper.apply(item1)).willReturn(vm1);
        given(mapper.apply(item2)).willReturn(vm2);

        ArgumentCaptor<MagicItemFilterCriteria> criteriaCaptor = ArgumentCaptor.forClass(MagicItemFilterCriteria.class);
        given(service.findAll(criteriaCaptor.capture())).willReturn(pagedResult);

        PagedMagicItemViewModel result = sut.findAll("sword", "rare", "weapon", true, 0, 50);

        assertThat(result.content()).containsExactly(vm1, vm2);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(50);
        assertThat(result.totalElements()).isEqualTo(2L);

        MagicItemFilterCriteria criteria = criteriaCaptor.getValue();
        assertThat(criteria.name()).isEqualTo("sword");
        assertThat(criteria.rarity()).isEqualTo("rare");
        assertThat(criteria.type()).isEqualTo("weapon");
        assertThat(criteria.requiresAttunement()).isTrue();
        assertThat(criteria.page()).isEqualTo(0);
        assertThat(criteria.pageSize()).isEqualTo(50);

        then(service).should().findAll(criteria);
        then(mapper).should().apply(item1);
        then(mapper).should().apply(item2);
    }
}
