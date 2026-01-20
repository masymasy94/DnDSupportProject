package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "magic_items")
public class MagicItemEntity extends PanacheEntity {

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_id", nullable = false)
    public MagicItemRarityEntity magicItemRarity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    public MagicItemTypeEntity magicItemType;

    @Column(name = "requires_attunement")
    public Boolean requiresAttunement;

    @Column(name = "attunement_requirements", length = 100)
    public String attunementRequirements;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "properties", columnDefinition = "JSONB")
    public String properties;

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

    public MagicItemEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public MagicItemRarityEntity getMagicItemRarity() {
        return magicItemRarity;
    }

    public MagicItemTypeEntity getMagicItemType() {
        return magicItemType;
    }

    public Boolean getRequiresAttunement() {
        return requiresAttunement;
    }

    public String getAttunementRequirements() {
        return attunementRequirements;
    }

    public String getDescription() {
        return description;
    }

    public String getProperties() {
        return properties;
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
