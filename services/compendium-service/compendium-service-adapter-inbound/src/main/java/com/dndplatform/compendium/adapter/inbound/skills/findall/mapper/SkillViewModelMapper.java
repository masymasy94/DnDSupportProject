package com.dndplatform.compendium.adapter.inbound.skills.findall.mapper;

import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface SkillViewModelMapper extends Function<Skill, SkillViewModel> {

    @Override
    SkillViewModel apply(Skill skill);
}
