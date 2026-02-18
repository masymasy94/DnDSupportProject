package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.repository.CampaignMemberFindByCampaignRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignMemberFindServiceImpl implements CampaignMemberFindService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignMemberFindByCampaignRepository repository;

    @Inject
    public CampaignMemberFindServiceImpl(CampaignMemberFindByCampaignRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CampaignMember> findByCampaignId(Long campaignId) {
        log.info(() -> "Finding members for campaign %d".formatted(campaignId));
        return repository.findByCampaignId(campaignId);
    }
}
