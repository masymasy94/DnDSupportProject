package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "species")
public class SpeciesEntity extends PanacheEntity {

    @Column(name = "name", length = 50, nullable = false)
    public String name;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

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

    public SpeciesEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
