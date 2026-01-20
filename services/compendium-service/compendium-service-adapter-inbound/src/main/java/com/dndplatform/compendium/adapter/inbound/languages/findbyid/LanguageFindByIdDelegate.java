package com.dndplatform.compendium.adapter.inbound.languages.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.languages.findall.mapper.LanguageViewModelMapper;
import com.dndplatform.compendium.domain.LanguageFindByIdService;
import com.dndplatform.compendium.view.model.LanguageFindByIdResource;
import com.dndplatform.compendium.view.model.vm.LanguageViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class LanguageFindByIdDelegate implements LanguageFindByIdResource {

    private final LanguageFindByIdService service;
    private final LanguageViewModelMapper mapper;

    @Inject
    public LanguageFindByIdDelegate(LanguageFindByIdService service,
                                    LanguageViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public LanguageViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}
