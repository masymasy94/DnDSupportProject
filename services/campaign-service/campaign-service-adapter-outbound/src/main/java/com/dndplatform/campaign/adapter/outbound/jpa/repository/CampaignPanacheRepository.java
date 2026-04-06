package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CampaignPanacheRepository implements PanacheRepository<CampaignEntity> {

    public PanacheQuery<CampaignEntity> findByMemberUserId(Long userId) {
        return find(
                "id IN (SELECT m.campaign.id FROM CampaignMemberEntity m WHERE m.userId = ?1)",
                userId
        );
    }

    public List<CampaignEntity> findByIdField(Long id) {
        return find("id", id).list();
    }
}
