package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "characters")
public class CharacterEntity extends PanacheEntity {

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    // Compendium service IDs (reference data stored in compendium-service)
    @Column(name = "compendium_species_id", nullable = false)
    public Integer compendiumSpeciesId;

    @Column(name = "compendium_class_id", nullable = false)
    public Integer compendiumClassId;

    // Denormalized names for display (cached from compendium-service)
    @Column(name = "species_name", length = 50)
    public String speciesName;

    @Column(name = "class_name", length = 30)
    public String className;

    @Column(name = "background_id")
    public Short backgroundId;

    @Column(name = "alignment_id")
    public Short alignmentId;

    @Column(name = "level", nullable = false)
    public Integer level = 1;

    @Column(name = "experience_points")
    public Integer experiencePoints = 0;

    // Ability Scores
    @Column(name = "strength", nullable = false)
    public Integer strength = 10;

    @Column(name = "dexterity", nullable = false)
    public Integer dexterity = 10;

    @Column(name = "constitution", nullable = false)
    public Integer constitution = 10;

    @Column(name = "intelligence", nullable = false)
    public Integer intelligence = 10;

    @Column(name = "wisdom", nullable = false)
    public Integer wisdom = 10;

    @Column(name = "charisma", nullable = false)
    public Integer charisma = 10;

    // Combat Stats
    @Column(name = "hit_points_current", nullable = false)
    public Integer hitPointsCurrent = 0;

    @Column(name = "hit_points_max", nullable = false)
    public Integer hitPointsMax = 0;

    @Column(name = "hit_points_temp")
    public Integer hitPointsTemp = 0;

    @Column(name = "armor_class", nullable = false)
    public Integer armorClass = 10;

    @Column(name = "speed", nullable = false)
    public Integer speed = 30;

    @Column(name = "hit_dice_total", nullable = false)
    public Integer hitDiceTotal = 1;

    @Column(name = "hit_dice_type", length = 4)
    public String hitDiceType = "d8";

    @Column(name = "hit_dice_used")
    public Integer hitDiceUsed = 0;

    @Column(name = "death_save_successes")
    public Integer deathSaveSuccesses = 0;

    @Column(name = "death_save_failures")
    public Integer deathSaveFailures = 0;

    // Currency
    @Column(name = "copper_pieces")
    public Integer copperPieces = 0;

    @Column(name = "silver_pieces")
    public Integer silverPieces = 0;

    @Column(name = "electrum_pieces")
    public Integer electrumPieces = 0;

    @Column(name = "gold_pieces")
    public Integer goldPieces = 0;

    @Column(name = "platinum_pieces")
    public Integer platinumPieces = 0;

    // Text fields
    @Column(name = "personality_traits", columnDefinition = "TEXT")
    public String personalityTraits;

    @Column(name = "ideals", columnDefinition = "TEXT")
    public String ideals;

    @Column(name = "bonds", columnDefinition = "TEXT")
    public String bonds;

    @Column(name = "flaws", columnDefinition = "TEXT")
    public String flaws;

    @Column(name = "backstory", columnDefinition = "TEXT")
    public String backstory;

    @Column(name = "appearance", columnDefinition = "TEXT")
    public String appearance;

    @Column(name = "notes", columnDefinition = "TEXT")
    public String notes;

    // Audit
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public CharacterEntity() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public String getClassName() {
        return className;
    }

    public Integer getCompendiumSpeciesId() {
        return compendiumSpeciesId;
    }

    public Integer getCompendiumClassId() {
        return compendiumClassId;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getHitPointsCurrent() {
        return hitPointsCurrent;
    }

    public Integer getHitPointsMax() {
        return hitPointsMax;
    }

    public Integer getArmorClass() {
        return armorClass;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
