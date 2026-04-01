package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.DifficultyResult;

public interface DifficultyCalculateService {
    DifficultyResult calculate(Long encounterId);
}
