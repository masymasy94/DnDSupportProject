package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.LanguageEntity;
import com.dndplatform.compendium.domain.model.Language;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface LanguageMapper extends Function<LanguageEntity, Language> {

    @Override
    Language apply(LanguageEntity entity);
}
