package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class SkillEntity extends PanacheEntity {

    @Column(name = "name", length = 30, nullable = false, unique = true)
    public String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ability_id", nullable = false)
    public AbilityEntity ability;

    public String getName() {
        return name;
    }

    public AbilityEntity getAbility() {
        return ability;
    }
}
