package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EncounterPanacheRepository implements PanacheRepository<EncounterEntity> {

    public Optional<EncounterEntity> findByIdOptional(Long id) {
        return find("id = ?1", id).firstResultOptional();
    }

    public List<EncounterEntity> findByCampaignId(Long campaignId) {
        return find("campaignId", campaignId).list();
    }
}
