package com.dndplatform.compendium.adapter.inbound.languages.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.languages.findall.mapper.LanguageViewModelMapper;
import com.dndplatform.compendium.domain.LanguageFindAllService;
import com.dndplatform.compendium.view.model.LanguageFindAllResource;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class LanguageFindAllDelegate implements LanguageFindAllResource {

    private final LanguageFindAllService service;
    private final LanguageViewModelMapper mapper;

    @Inject
    public LanguageFindAllDelegate(LanguageFindAllService service,
                                   LanguageViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<LanguageViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}
