package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_saving_throws")
public class CharacterSavingThrowEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ability_id", nullable = false)
    public AbilityEntity ability;

    @Column(name = "proficient")
    public Boolean proficient = false;

    public CharacterEntity getCharacter() {
        return character;
    }

    public AbilityEntity getAbility() {
        return ability;
    }

    public Boolean getProficient() {
        return proficient;
    }
}
