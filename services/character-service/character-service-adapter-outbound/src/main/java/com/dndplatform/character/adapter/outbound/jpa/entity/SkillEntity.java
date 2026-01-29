package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class SkillEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Short id;

    @Column(name = "name", length = 20, nullable = false, unique = true)
    public String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ability_id", nullable = false)
    public AbilityEntity ability;

    public Short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AbilityEntity getAbility() {
        return ability;
    }
}
