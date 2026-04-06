package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ParticipantPanacheRepository implements PanacheRepository<EncounterParticipantEntity> {

    public Optional<EncounterParticipantEntity> findByIdOptional(Long id) {
        return find("id = ?1", id).firstResultOptional();
    }

    public List<EncounterParticipantEntity> findByEncounterIdOrderBySortOrder(Long encounterId) {
        return find("encounter.id = ?1 order by sortOrder asc", encounterId).list();
    }
}
