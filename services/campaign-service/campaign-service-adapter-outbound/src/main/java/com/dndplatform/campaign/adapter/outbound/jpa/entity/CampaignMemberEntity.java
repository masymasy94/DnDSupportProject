package com.dndplatform.campaign.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaign_members")
public class CampaignMemberEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    public CampaignEntity campaign;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "character_id")
    public Long characterId;

    @Column(name = "role", length = 20, nullable = false)
    public String role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    public LocalDateTime joinedAt;

    public CampaignMemberEntity() {
        this.joinedAt = LocalDateTime.now();
        this.role = "PLAYER";
    }
}
