package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CampaignNotePanacheRepository implements PanacheRepository<CampaignNoteEntity> {

    public List<CampaignNoteEntity> findVisibleNotes(Long campaignId, Long userId) {
        return find(
                "campaign.id = ?1 and (visibility = 'PUBLIC' or authorId = ?2)",
                campaignId, userId
        ).list();
    }
}
