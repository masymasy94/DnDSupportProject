package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CharacterPanacheRepository implements PanacheRepository<CharacterEntity> {

    public Optional<CharacterEntity> findByIdAndUserId(Long characterId, Long userId) {
        return find("id = ?1 and userId = ?2", characterId, userId).firstResultOptional();
    }

    public List<CharacterEntity> findAllPaged(int page, int size) {
        return findAll().page(Page.of(page, size)).list();
    }

    public long countAll() {
        return count();
    }
}
