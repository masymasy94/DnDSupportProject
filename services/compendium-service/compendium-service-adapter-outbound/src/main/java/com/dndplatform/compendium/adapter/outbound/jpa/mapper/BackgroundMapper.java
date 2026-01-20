package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.BackgroundEntity;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.model.SourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.function.Function;

@Mapper
public interface BackgroundMapper extends Function<BackgroundEntity, Background> {

    @Override
    @Mapping(target = "source", source = "source", qualifiedByName = "toSourceType")
    @Mapping(target = "isPublic", source = "isPublic", defaultValue = "false")
    Background apply(BackgroundEntity entity);

    @Named("toSourceType")
    default SourceType toSourceType(String source) {
        if (source == null) {
            return SourceType.OFFICIAL;
        }
        return SourceType.valueOf(source.toUpperCase());
    }
}
