package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.CharacterClass;

import java.util.Optional;

public interface CharacterClassFindByIdService {
    CharacterClass findById(int id);
}
