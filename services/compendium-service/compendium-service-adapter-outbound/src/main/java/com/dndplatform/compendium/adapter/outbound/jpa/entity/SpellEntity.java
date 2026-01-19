package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "spells")
public class SpellEntity extends PanacheEntity {

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "level", nullable = false)
    public Integer level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    public SpellSchoolEntity spellSchool;

    @Column(name = "casting_time", length = 100)
    public String castingTime;

    @Column(name = "spell_range", length = 100)
    public String spellRange;

    @Column(name = "components", length = 20)
    public String components;

    @Column(name = "material_components", columnDefinition = "TEXT")
    public String materialComponents;

    @Column(name = "duration", length = 100)
    public String duration;

    @Column(name = "concentration")
    public Boolean concentration;

    @Column(name = "ritual")
    public Boolean ritual;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "higher_levels", columnDefinition = "TEXT")
    public String higherLevels;

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

    public SpellEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public Integer getLevel() {
        return level;
    }

    public SpellSchoolEntity getSpellSchool() {
        return spellSchool;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public String getSpellRange() {
        return spellRange;
    }

    public String getComponents() {
        return components;
    }

    public String getMaterialComponents() {
        return materialComponents;
    }

    public String getDuration() {
        return duration;
    }

    public Boolean getConcentration() {
        return concentration;
    }

    public Boolean getRitual() {
        return ritual;
    }

    public String getDescription() {
        return description;
    }

    public String getHigherLevels() {
        return higherLevels;
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
