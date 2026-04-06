package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.SkillEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SkillPanacheRepository implements PanacheRepositoryBase<SkillEntity, Short> {

    public Optional<SkillEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
