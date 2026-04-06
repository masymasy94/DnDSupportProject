package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.domain.repository.CampaignQuestDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestDeleteRepositoryJpa implements CampaignQuestDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestPanacheRepository panacheRepository;

    @Inject
    public CampaignQuestDeleteRepositoryJpa(CampaignQuestPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long questId) {
        log.info(() -> "Deleting quest: %d".formatted(questId));

        CampaignQuestEntity entity = panacheRepository.findById(questId);
        if (entity == null) {
            throw new NotFoundException("Quest not found with ID: %d".formatted(questId));
        }

        panacheRepository.delete(entity);
        log.info(() -> "Quest %d deleted successfully".formatted(questId));
    }
}
