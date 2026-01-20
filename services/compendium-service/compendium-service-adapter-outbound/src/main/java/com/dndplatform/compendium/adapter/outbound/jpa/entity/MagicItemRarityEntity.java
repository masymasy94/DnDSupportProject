package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "magic_item_rarities")
public class MagicItemRarityEntity extends PanacheEntity {

    @Column(name = "name", length = 20, nullable = false, unique = true)
    public String name;

    public String getName() {
        return name;
    }
}
