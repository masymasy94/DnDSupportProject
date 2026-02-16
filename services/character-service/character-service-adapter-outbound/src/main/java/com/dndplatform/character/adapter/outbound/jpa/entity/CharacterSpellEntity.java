package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_spells")
public class CharacterSpellEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spell_id", nullable = false)
    public SpellEntity spell;

    @Column(name = "prepared")
    public Boolean prepared = false;

    @Column(name = "source", length = 50)
    public String source;

    public CharacterEntity getCharacter() {
        return character;
    }

    public SpellEntity getSpell() {
        return spell;
    }

    public Boolean getPrepared() {
        return prepared;
    }

    public String getSource() {
        return source;
    }
}
