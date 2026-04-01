package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.domain.repository.CampaignDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignDeleteRepositoryJpa implements CampaignDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info(() -> "Deleting campaign: %d".formatted(id));

        CampaignEntity entity = CampaignEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException("Campaign not found with ID: %d".formatted(id));
        }

        entity.delete();
        log.info(() -> "Campaign %d deleted successfully".formatted(id));
    }
}
