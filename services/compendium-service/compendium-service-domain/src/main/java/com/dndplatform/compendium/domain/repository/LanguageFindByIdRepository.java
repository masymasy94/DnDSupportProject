package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Language;

import java.util.Optional;

public interface LanguageFindByIdRepository {
    Optional<Language> findById(int id);
}
