package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_skills")
public class CharacterSkillEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    public SkillEntity skill;

    @Column(name = "proficient")
    public Boolean proficient = false;

    @Column(name = "expertise")
    public Boolean expertise = false;

    public CharacterEntity getCharacter() {
        return character;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public Boolean getProficient() {
        return proficient;
    }

    public Boolean getExpertise() {
        return expertise;
    }
}
