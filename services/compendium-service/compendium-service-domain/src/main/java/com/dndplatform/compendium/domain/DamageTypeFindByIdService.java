package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.DamageType;

public interface DamageTypeFindByIdService {
    DamageType findById(int id);
}
