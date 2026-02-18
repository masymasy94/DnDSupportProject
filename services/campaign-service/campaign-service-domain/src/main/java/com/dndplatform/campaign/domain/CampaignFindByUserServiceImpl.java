package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.PagedResult;
import com.dndplatform.campaign.domain.repository.CampaignFindByUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignFindByUserServiceImpl implements CampaignFindByUserService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByUserRepository repository;

    @Inject
    public CampaignFindByUserServiceImpl(CampaignFindByUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public PagedResult findByUserId(Long userId, int page, int size) {
        log.info(() -> "Finding campaigns for user %d - page: %d, size: %d".formatted(userId, page, size));
        return repository.findByUserId(userId, page, size);
    }
}
