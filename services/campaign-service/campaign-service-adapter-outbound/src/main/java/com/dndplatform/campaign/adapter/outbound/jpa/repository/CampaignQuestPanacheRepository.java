package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CampaignQuestPanacheRepository implements PanacheRepository<CampaignQuestEntity> {

    public List<CampaignQuestEntity> findByCampaignId(Long campaignId) {
        return find("campaign.id", campaignId).list();
    }

    public List<CampaignQuestEntity> findByCampaignIdAndStatus(Long campaignId, String status) {
        return find("campaign.id = ?1 and status = ?2", campaignId, status).list();
    }
}
