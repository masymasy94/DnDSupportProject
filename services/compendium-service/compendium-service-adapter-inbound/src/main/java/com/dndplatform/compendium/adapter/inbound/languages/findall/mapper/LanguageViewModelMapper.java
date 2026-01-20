package com.dndplatform.compendium.adapter.inbound.languages.findall.mapper;

import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface LanguageViewModelMapper extends Function<Language, LanguageViewModel> {

    @Override
    LanguageViewModel apply(Language language);
}
