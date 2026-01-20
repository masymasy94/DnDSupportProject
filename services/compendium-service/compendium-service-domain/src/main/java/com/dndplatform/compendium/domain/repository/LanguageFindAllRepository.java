package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Language;

import java.util.List;

public interface LanguageFindAllRepository {
    List<Language> findAllLanguages();
}
