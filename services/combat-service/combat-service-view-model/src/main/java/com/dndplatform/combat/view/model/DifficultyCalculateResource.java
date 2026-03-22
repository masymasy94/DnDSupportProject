package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.DifficultyResultViewModel;

public interface DifficultyCalculateResource {
    DifficultyResultViewModel calculate(Long encounterId);
}
