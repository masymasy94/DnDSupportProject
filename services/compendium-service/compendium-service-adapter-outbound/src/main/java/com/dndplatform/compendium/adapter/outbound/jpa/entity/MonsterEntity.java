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
@Table(name = "monsters")
public class MonsterEntity extends PanacheEntity {

    @Column(name = "index", length = 100, nullable = false)
    public String index;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id", nullable = false)
    public MonsterSizeEntity monsterSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    public MonsterTypeEntity monsterType;

    @Column(name = "subtype", length = 50)
    public String subtype;

    @Column(name = "alignment", length = 30)
    public String alignment;

    @Column(name = "armor_class", nullable = false)
    public Integer armorClass;

    @Column(name = "armor_type", length = 50)
    public String armorType;

    @Column(name = "hit_points", nullable = false)
    public Integer hitPoints;

    @Column(name = "hit_dice", length = 20, nullable = false)
    public String hitDice;

    @Column(name = "speed", columnDefinition = "jsonb", nullable = false)
    public String speed;

    @Column(name = "strength", nullable = false)
    public Integer strength;

    @Column(name = "dexterity", nullable = false)
    public Integer dexterity;

    @Column(name = "constitution", nullable = false)
    public Integer constitution;

    @Column(name = "intelligence", nullable = false)
    public Integer intelligence;

    @Column(name = "wisdom", nullable = false)
    public Integer wisdom;

    @Column(name = "charisma", nullable = false)
    public Integer charisma;

    @Column(name = "saving_throws", columnDefinition = "jsonb")
    public String savingThrows;

    @Column(name = "skills", columnDefinition = "jsonb")
    public String skills;

    @Column(name = "senses", columnDefinition = "jsonb")
    public String senses;

    @Column(name = "languages", length = 255)
    public String languages;

    @Column(name = "challenge_rating", length = 10, nullable = false)
    public String challengeRating;

    @Column(name = "xp", nullable = false)
    public Integer xp;

    @Column(name = "proficiency_bonus", nullable = false)
    public Integer proficiencyBonus;

    @Column(name = "special_abilities", columnDefinition = "jsonb")
    public String specialAbilities;

    @Column(name = "actions", columnDefinition = "jsonb")
    public String actions;

    @Column(name = "reactions", columnDefinition = "jsonb")
    public String reactions;

    @Column(name = "legendary_actions", columnDefinition = "jsonb")
    public String legendaryActions;

    @Column(name = "legendary_desc", columnDefinition = "TEXT")
    public String legendaryDesc;

    @Column(name = "lair_actions", columnDefinition = "jsonb")
    public String lairActions;

    @Column(name = "regional_effects", columnDefinition = "jsonb")
    public String regionalEffects;

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

    public MonsterEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public MonsterSizeEntity getMonsterSize() {
        return monsterSize;
    }

    public MonsterTypeEntity getMonsterType() {
        return monsterType;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getAlignment() {
        return alignment;
    }

    public Integer getArmorClass() {
        return armorClass;
    }

    public String getArmorType() {
        return armorType;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public String getHitDice() {
        return hitDice;
    }

    public String getSpeed() {
        return speed;
    }

    public Integer getStrength() {
        return strength;
    }

    public Integer getDexterity() {
        return dexterity;
    }

    public Integer getConstitution() {
        return constitution;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public Integer getWisdom() {
        return wisdom;
    }

    public Integer getCharisma() {
        return charisma;
    }

    public String getSavingThrows() {
        return savingThrows;
    }

    public String getSkills() {
        return skills;
    }

    public String getSenses() {
        return senses;
    }

    public String getLanguages() {
        return languages;
    }

    public String getChallengeRating() {
        return challengeRating;
    }

    public Integer getXp() {
        return xp;
    }

    public Integer getProficiencyBonus() {
        return proficiencyBonus;
    }

    public String getSpecialAbilities() {
        return specialAbilities;
    }

    public String getActions() {
        return actions;
    }

    public String getReactions() {
        return reactions;
    }

    public String getLegendaryActions() {
        return legendaryActions;
    }

    public String getLegendaryDesc() {
        return legendaryDesc;
    }

    public String getLairActions() {
        return lairActions;
    }

    public String getRegionalEffects() {
        return regionalEffects;
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
