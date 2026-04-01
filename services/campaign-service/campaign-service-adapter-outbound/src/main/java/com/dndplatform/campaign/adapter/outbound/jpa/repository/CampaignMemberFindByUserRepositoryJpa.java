package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.repository.CampaignMemberFindByUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CampaignMemberFindByUserRepositoryJpa implements CampaignMemberFindByUserRepository {

    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignMemberFindByUserRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<CampaignMember> findByCampaignIdAndUserId(Long campaignId, Long userId) {
        CampaignMemberEntity entity = CampaignMemberEntity
                .find("campaign.id = ?1 and userId = ?2", campaignId, userId)
                .firstResult();
        return Optional.ofNullable(entity).map(mapper::toCampaignMember);
    }
}
