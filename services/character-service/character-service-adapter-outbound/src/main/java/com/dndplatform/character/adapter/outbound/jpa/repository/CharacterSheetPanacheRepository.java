package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterSheetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CharacterSheetPanacheRepository implements PanacheRepository<CharacterSheetEntity> {

    public Optional<CharacterSheetEntity> findByCharacterId(Long characterId) {
        return find("characterId", characterId).firstResultOptional();
    }

    public long deleteByCharacterId(Long characterId) {
        return delete("characterId", characterId);
    }
}
