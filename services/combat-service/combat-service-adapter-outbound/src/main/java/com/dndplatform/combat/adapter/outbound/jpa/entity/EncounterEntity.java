package com.dndplatform.combat.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "encounters")
public class EncounterEntity extends PanacheEntity {

    @Column(name = "campaign_id", nullable = false)
    public Long campaignId;

    @Column(name = "created_by_user_id", nullable = false)
    public Long createdByUserId;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "status", length = 20, nullable = false)
    public String status;

    @Column(name = "party_level", nullable = false)
    public Integer partyLevel;

    @Column(name = "party_size", nullable = false)
    public Integer partySize;

    @Column(name = "difficulty_rating", length = 20)
    public String difficultyRating;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sortOrder ASC")
    public List<EncounterParticipantEntity> participants = new ArrayList<>();

    public EncounterEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT";
        this.partyLevel = 1;
        this.partySize = 4;
    }
}
