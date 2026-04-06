package com.dndplatform.campaign.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaign_notes")
public class CampaignNoteEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    public CampaignEntity campaign;

    @Column(name = "author_id", nullable = false)
    public Long authorId;

    @Column(name = "title", length = 100, nullable = false)
    public String title;

    @Column(name = "content", columnDefinition = "TEXT")
    public String content;

    @Column(name = "visibility", length = 10, nullable = false)
    public String visibility;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public CampaignNoteEntity() {
        this.createdAt = LocalDateTime.now();
        this.visibility = "PUBLIC";
    }
}
