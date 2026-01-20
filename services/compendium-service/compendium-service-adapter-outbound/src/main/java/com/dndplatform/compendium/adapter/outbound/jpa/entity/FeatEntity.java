package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "feats")
public class FeatEntity extends PanacheEntity {

    @Column(name = "name", length = 60, nullable = false)
    public String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    public String description;

    @Column(name = "prerequisite", length = 255)
    public String prerequisite;

    @Column(name = "prerequisite_ability", length = 3)
    public String prerequisiteAbility;

    @Column(name = "prerequisite_level")
    public Integer prerequisiteLevel;

    @Column(name = "benefit", columnDefinition = "TEXT")
    public String benefit;

    @Column(name = "grants_ability_increase")
    public Boolean grantsAbilityIncrease;

    @Column(name = "source", length = 10, nullable = false)
    public String source;

    @Column(name = "owner_id")
    public Long ownerId;

    @Column(name = "campaign_id")
    public Long campaignId;

    @Column(name = "is_public")
    public Boolean isPublic;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public FeatEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public String getPrerequisiteAbility() {
        return prerequisiteAbility;
    }

    public Integer getPrerequisiteLevel() {
        return prerequisiteLevel;
    }

    public String getBenefit() {
        return benefit;
    }

    public Boolean getGrantsAbilityIncrease() {
        return grantsAbilityIncrease;
    }

    public String getSource() {
        return source;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }
}
