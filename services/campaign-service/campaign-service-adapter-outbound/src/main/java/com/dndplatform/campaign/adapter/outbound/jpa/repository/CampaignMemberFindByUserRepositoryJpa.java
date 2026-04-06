package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.repository.CampaignMemberFindByUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CampaignMemberFindByUserRepositoryJpa implements CampaignMemberFindByUserRepository {

    private final CampaignMemberPanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignMemberFindByUserRepositoryJpa(CampaignMemberPanacheRepository panacheRepository,
                                                 CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CampaignMember> findByCampaignIdAndUserId(Long campaignId, Long userId) {
        return panacheRepository.findByCampaignIdAndUserId(campaignId, userId)
                .map(mapper::toCampaignMember);
    }
}
