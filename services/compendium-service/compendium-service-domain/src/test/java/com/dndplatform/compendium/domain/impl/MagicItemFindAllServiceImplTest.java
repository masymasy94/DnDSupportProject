package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.MagicItemFindAllService;
import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MagicItemFindAllRepository;
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
class MagicItemFindAllServiceImplTest {

    @Mock
    private MagicItemFindAllRepository repository;

    private MagicItemFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new MagicItemFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random MagicItem magicItem) {
        MagicItemFilterCriteria criteria = new MagicItemFilterCriteria(null, null, null, null, 0, 20);
        List<MagicItem> items = List.of(magicItem);
        PagedResult<MagicItem> expected = new PagedResult<>(items, 0, 20, 1);
        given(repository.findAllMagicItems(criteria)).willReturn(expected);

        PagedResult<MagicItem> result = sut.findAll(criteria);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllMagicItems(criteria);
        inOrder.verifyNoMoreInteractions();
    }
}
