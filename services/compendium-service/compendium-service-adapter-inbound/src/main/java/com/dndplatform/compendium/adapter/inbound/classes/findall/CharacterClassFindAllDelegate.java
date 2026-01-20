package com.dndplatform.compendium.adapter.inbound.classes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.classes.findall.mapper.CharacterClassViewModelMapper;
import com.dndplatform.compendium.domain.CharacterClassFindAllService;
import com.dndplatform.compendium.view.model.CharacterClassFindAllResource;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class CharacterClassFindAllDelegate implements CharacterClassFindAllResource {

    private final CharacterClassFindAllService service;
    private final CharacterClassViewModelMapper mapper;

    @Inject
    public CharacterClassFindAllDelegate(CharacterClassFindAllService service,
                                         CharacterClassViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<CharacterClassViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}
