package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemEntity;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.SourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.function.Function;

@Mapper
public interface MagicItemMapper extends Function<MagicItemEntity, MagicItem> {

    @Override
    @Mapping(target = "rarity", source = "magicItemRarity.name")
    @Mapping(target = "type", source = "magicItemType.name")
    @Mapping(target = "requiresAttunement", source = "requiresAttunement", defaultValue = "false")
    @Mapping(target = "source", source = "source", qualifiedByName = "toSourceType")
    @Mapping(target = "isPublic", source = "isPublic", defaultValue = "false")
    MagicItem apply(MagicItemEntity entity);

    @Named("toSourceType")
    default SourceType toSourceType(String source) {
        if (source == null) {
            return SourceType.OFFICIAL;
        }
        return SourceType.valueOf(source.toUpperCase());
    }
}
