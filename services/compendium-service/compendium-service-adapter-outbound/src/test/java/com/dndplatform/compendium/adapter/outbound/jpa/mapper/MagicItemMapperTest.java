package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemRarityEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemTypeEntity;
import com.dndplatform.compendium.domain.model.SourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class MagicItemMapperTest {

    private final MagicItemMapper sut = Mappers.getMapper(MagicItemMapper.class);

    @Test
    void shouldMapToMagicItem(@Random MagicItemEntity entity) {
        var rarity = new MagicItemRarityEntity();
        rarity.name = "Rare";
        entity.magicItemRarity = rarity;

        var type = new MagicItemTypeEntity();
        type.name = "Armor";
        entity.magicItemType = type;

        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.rarity()).isEqualTo("Rare");
        assertThat(result.type()).isEqualTo("Armor");
        assertThat(result.source()).isEqualTo(SourceType.OFFICIAL);
        assertThat(result.ownerId()).isEqualTo(entity.ownerId);
        assertThat(result.campaignId()).isEqualTo(entity.campaignId);
    }

    @Test
    void shouldDefaultRequiresAttunementToFalseWhenNull(@Random MagicItemEntity entity) {
        var rarity = new MagicItemRarityEntity();
        rarity.name = "Common";
        entity.magicItemRarity = rarity;

        var type = new MagicItemTypeEntity();
        type.name = "Wondrous Item";
        entity.magicItemType = type;

        entity.source = null;
        entity.requiresAttunement = null;

        var result = sut.apply(entity);

        assertThat(result.requiresAttunement()).isFalse();
    }
}
