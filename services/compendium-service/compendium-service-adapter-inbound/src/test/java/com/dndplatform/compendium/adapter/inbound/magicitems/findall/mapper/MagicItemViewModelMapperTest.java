package com.dndplatform.compendium.adapter.inbound.magicitems.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class MagicItemViewModelMapperTest {

    private final MagicItemViewModelMapper sut = Mappers.getMapper(MagicItemViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random MagicItem magicItem) {
        MagicItemViewModel result = sut.apply(magicItem);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(magicItem.id());
        assertThat(result.name()).isEqualTo(magicItem.name());
        assertThat(result.rarity()).isEqualTo(magicItem.rarity());
        assertThat(result.type()).isEqualTo(magicItem.type());
        assertThat(result.source()).isEqualTo(magicItem.source().name());
    }
}
