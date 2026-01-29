package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "character_features")
public class CharacterFeatureEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "source", length = 50)
    public String source;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    public CharacterEntity getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }
}
