package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.repository.CampaignMemberFindByCampaignRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignMemberFindByCampaignRepositoryJpa implements CampaignMemberFindByCampaignRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignMemberFindByCampaignRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<CampaignMember> findByCampaignId(Long campaignId) {
        log.info(() -> "Finding members for campaign %d".formatted(campaignId));

        return CampaignMemberEntity.<CampaignMemberEntity>find("campaign.id", campaignId)
                .list().stream()
                .map(mapper::toCampaignMember)
                .toList();
    }
}
