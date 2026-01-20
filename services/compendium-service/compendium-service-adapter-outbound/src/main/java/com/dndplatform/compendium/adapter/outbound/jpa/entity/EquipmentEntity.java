package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
public class EquipmentEntity extends PanacheEntity {

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    public EquipmentCategoryEntity equipmentCategory;

    @Column(name = "cost_gp", precision = 10, scale = 2)
    public BigDecimal costGp;

    @Column(name = "cost_display", length = 30)
    public String costDisplay;

    @Column(name = "weight_lb", precision = 8, scale = 2)
    public BigDecimal weightLb;

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

    public EquipmentEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public EquipmentCategoryEntity getEquipmentCategory() {
        return equipmentCategory;
    }

    public BigDecimal getCostGp() {
        return costGp;
    }

    public String getCostDisplay() {
        return costDisplay;
    }

    public BigDecimal getWeightLb() {
        return weightLb;
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
