package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "character_equipment")
public class CharacterEquipmentEntity extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    public CharacterEntity character;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "quantity")
    public Integer quantity = 1;

    @Column(name = "weight", precision = 10, scale = 2)
    public BigDecimal weight;

    @Column(name = "equipped")
    public Boolean equipped = false;

    public CharacterEntity getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Boolean getEquipped() {
        return equipped;
    }
}
