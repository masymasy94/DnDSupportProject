package com.dndplatform.combat.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "encounter_participants")
public class EncounterParticipantEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    public EncounterEntity encounter;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "participant_type", length = 10, nullable = false)
    public String participantType;

    @Column(name = "initiative")
    public Integer initiative;

    @Column(name = "current_hp", nullable = false)
    public Integer currentHp;

    @Column(name = "max_hp", nullable = false)
    public Integer maxHp;

    @Column(name = "armor_class", nullable = false)
    public Integer armorClass;

    @Column(name = "conditions", columnDefinition = "TEXT", nullable = false)
    public String conditions;

    @Column(name = "is_active", nullable = false)
    public Boolean isActive;

    @Column(name = "sort_order", nullable = false)
    public Integer sortOrder;

    @Column(name = "monster_id")
    public Long monsterId;

    @Column(name = "source_json", columnDefinition = "TEXT")
    public String sourceJson;

    public EncounterParticipantEntity() {
        this.initiative = 0;
        this.armorClass = 10;
        this.conditions = "[]";
        this.isActive = false;
        this.sortOrder = 0;
    }
}
