package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.LanguageEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class LanguagePanacheRepository implements PanacheRepositoryBase<LanguageEntity, Short> {

    public Optional<LanguageEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
