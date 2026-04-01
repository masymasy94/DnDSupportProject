package com.dndplatform.campaign.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaign_quests")
public class CampaignQuestEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    public CampaignEntity campaign;

    @Column(name = "author_id", nullable = false)
    public Long authorId;

    @Column(name = "title", length = 100, nullable = false)
    public String title;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "status", length = 20, nullable = false)
    public String status;

    @Column(name = "priority", length = 10, nullable = false)
    public String priority;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public CampaignQuestEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
        this.priority = "MAIN";
    }
}
