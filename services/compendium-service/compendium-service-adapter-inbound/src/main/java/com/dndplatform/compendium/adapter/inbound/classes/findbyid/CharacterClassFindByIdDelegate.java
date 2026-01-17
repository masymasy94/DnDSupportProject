package com.dndplatform.compendium.adapter.inbound.classes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.classes.findall.mapper.CharacterClassViewModelMapper;
import com.dndplatform.compendium.domain.CharacterClassFindByIdService;
import com.dndplatform.compendium.view.model.CharacterClassFindByIdResource;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@Delegate
@RequestScoped
public class CharacterClassFindByIdDelegate implements CharacterClassFindByIdResource {

    private final CharacterClassFindByIdService service;
    private final CharacterClassViewModelMapper mapper;

    @Inject
    public CharacterClassFindByIdDelegate(CharacterClassFindByIdService service,
                                          CharacterClassViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public CharacterClassViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}
