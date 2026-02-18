package com.dndplatform.campaign.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campaigns")
public class CampaignEntity extends PanacheEntity {

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "dungeon_master_id", nullable = false)
    public Long dungeonMasterId;

    @Column(name = "status", length = 20, nullable = false)
    public String status;

    @Column(name = "max_players", nullable = false)
    public Integer maxPlayers;

    @Column(name = "image_url", length = 255)
    public String imageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CampaignMemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CampaignNoteEntity> notes = new ArrayList<>();

    public CampaignEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT";
        this.maxPlayers = 6;
    }
}
