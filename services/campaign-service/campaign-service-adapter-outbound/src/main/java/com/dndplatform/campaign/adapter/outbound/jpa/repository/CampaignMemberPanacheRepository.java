package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CampaignMemberPanacheRepository implements PanacheRepository<CampaignMemberEntity> {

    public List<CampaignMemberEntity> findByCampaignId(Long campaignId) {
        return find("campaign.id", campaignId).list();
    }

    public Optional<CampaignMemberEntity> findByCampaignIdAndUserId(Long campaignId, Long userId) {
        return find("campaign.id = ?1 and userId = ?2", campaignId, userId).firstResultOptional();
    }

    public long deleteByCampaignIdAndUserId(Long campaignId, Long userId) {
        return delete("campaign.id = ?1 and userId = ?2", campaignId, userId);
    }
}
