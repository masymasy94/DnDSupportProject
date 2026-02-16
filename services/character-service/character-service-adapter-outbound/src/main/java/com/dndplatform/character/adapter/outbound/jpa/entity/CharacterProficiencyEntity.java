package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_proficiencies")
public class CharacterProficiencyEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proficiency_type_id", nullable = false)
    public ProficiencyTypeEntity proficiencyType;

    @Column(name = "name", length = 50, nullable = false)
    public String name;

    public CharacterEntity getCharacter() {
        return character;
    }

    public ProficiencyTypeEntity getProficiencyType() {
        return proficiencyType;
    }

    public String getName() {
        return name;
    }
}
