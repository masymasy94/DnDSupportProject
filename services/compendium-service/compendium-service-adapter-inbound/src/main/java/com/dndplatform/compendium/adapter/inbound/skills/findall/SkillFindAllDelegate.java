package com.dndplatform.compendium.adapter.inbound.skills.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.skills.findall.mapper.SkillViewModelMapper;
import com.dndplatform.compendium.domain.SkillFindAllService;
import com.dndplatform.compendium.view.model.SkillFindAllResource;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class SkillFindAllDelegate implements SkillFindAllResource {

    private final SkillFindAllService service;
    private final SkillViewModelMapper mapper;

    @Inject
    public SkillFindAllDelegate(SkillFindAllService service,
                                SkillViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<SkillViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}
