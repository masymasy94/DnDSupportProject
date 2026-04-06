package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.AbilityEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class AbilityPanacheRepository implements PanacheRepositoryBase<AbilityEntity, Short> {

    public Optional<AbilityEntity> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }
}
