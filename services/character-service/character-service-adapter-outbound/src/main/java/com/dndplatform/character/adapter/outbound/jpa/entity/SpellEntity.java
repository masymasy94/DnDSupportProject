package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "spells")
public class SpellEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    public String name;

    @Column(name = "level", nullable = false)
    public Short level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "school_id", nullable = false)
    public SpellSchoolEntity school;

    @Column(name = "casting_time", length = 30, nullable = false)
    public String castingTime;

    @Column(name = "spell_range", length = 30, nullable = false)
    public String spellRange;

    @Column(name = "components", length = 10, nullable = false)
    public String components;

    @Column(name = "material_components", columnDefinition = "TEXT")
    public String materialComponents;

    @Column(name = "duration", length = 30, nullable = false)
    public String duration;

    @Column(name = "concentration")
    public Boolean concentration = false;

    @Column(name = "ritual")
    public Boolean ritual = false;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Short getLevel() {
        return level;
    }

    public SpellSchoolEntity getSchool() {
        return school;
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
}
