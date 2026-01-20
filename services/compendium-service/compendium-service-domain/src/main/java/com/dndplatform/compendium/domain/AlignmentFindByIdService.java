package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Alignment;

public interface AlignmentFindByIdService {
    Alignment findById(int id);
}
