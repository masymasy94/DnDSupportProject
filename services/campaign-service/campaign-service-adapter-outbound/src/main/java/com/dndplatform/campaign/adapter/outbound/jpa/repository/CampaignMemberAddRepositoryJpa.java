package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.campaign.domain.repository.CampaignMemberAddRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignMemberAddRepositoryJpa implements CampaignMemberAddRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignMemberAddRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CampaignMember add(Long campaignId, Long userId, Long characterId, MemberRole role) {
        log.info(() -> "Adding user %d to campaign %d with role %s".formatted(userId, campaignId, role));

        CampaignEntity campaign = CampaignEntity.findById(campaignId);
        if (campaign == null) {
            throw new NotFoundException("Campaign not found with ID: %d".formatted(campaignId));
        }

        CampaignMemberEntity entity = new CampaignMemberEntity();
        entity.campaign = campaign;
        entity.userId = userId;
        entity.characterId = characterId;
        entity.role = role.name();
        entity.joinedAt = LocalDateTime.now();

        entity.persist();

        log.info(() -> "Member added with ID: %d".formatted(entity.id));
        return mapper.toCampaignMember(entity);
    }
}
