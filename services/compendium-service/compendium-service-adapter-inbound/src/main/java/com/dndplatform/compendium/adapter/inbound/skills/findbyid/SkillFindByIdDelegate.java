package com.dndplatform.compendium.adapter.inbound.skills.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.skills.findall.mapper.SkillViewModelMapper;
import com.dndplatform.compendium.domain.SkillFindByIdService;
import com.dndplatform.compendium.view.model.SkillFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class SkillFindByIdDelegate implements SkillFindByIdResource {

    private final SkillFindByIdService service;
    private final SkillViewModelMapper mapper;

    @Inject
    public SkillFindByIdDelegate(SkillFindByIdService service,
                                 SkillViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public SkillViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}
