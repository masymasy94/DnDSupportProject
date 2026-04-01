package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignFindByIdServiceImpl implements CampaignFindByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository repository;

    @Inject
    public CampaignFindByIdServiceImpl(CampaignFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Campaign findById(Long id) {
        log.info(() -> "Finding campaign by ID: %d".formatted(id));
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(id)));
    }
}
