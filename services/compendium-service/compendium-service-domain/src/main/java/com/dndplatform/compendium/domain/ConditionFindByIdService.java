package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Condition;

public interface ConditionFindByIdService {
    Condition findById(int id);
}
