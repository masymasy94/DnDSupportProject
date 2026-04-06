package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.SpellEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SpellPanacheRepository implements PanacheRepositoryBase<SpellEntity, Integer> {

    public Optional<SpellEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
