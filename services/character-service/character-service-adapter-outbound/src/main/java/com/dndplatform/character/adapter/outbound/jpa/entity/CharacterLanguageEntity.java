package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_languages")
public class CharacterLanguageEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", nullable = false)
    public LanguageEntity language;

    @Column(name = "source", length = 20)
    public String source;

    public CharacterEntity getCharacter() {
        return character;
    }

    public LanguageEntity getLanguage() {
        return language;
    }

    public String getSource() {
        return source;
    }
}
