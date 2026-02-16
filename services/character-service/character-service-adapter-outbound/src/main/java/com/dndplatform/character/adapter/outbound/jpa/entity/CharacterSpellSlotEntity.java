package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_spell_slots")
public class CharacterSpellSlotEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @Column(name = "spell_level", nullable = false)
    public Short spellLevel;

    @Column(name = "slots_total", nullable = false)
    public Short slotsTotal = 0;

    @Column(name = "slots_used", nullable = false)
    public Short slotsUsed = 0;

    public CharacterEntity getCharacter() {
        return character;
    }

    public Short getSpellLevel() {
        return spellLevel;
    }

    public Short getSlotsTotal() {
        return slotsTotal;
    }

    public Short getSlotsUsed() {
        return slotsUsed;
    }
}
