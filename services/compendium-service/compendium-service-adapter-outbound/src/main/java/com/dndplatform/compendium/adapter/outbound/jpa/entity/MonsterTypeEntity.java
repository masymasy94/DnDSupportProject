package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "monster_types")
public class MonsterTypeEntity extends PanacheEntity {

    @Column(name = "name", length = 30, nullable = false, unique = true)
    public String name;

    public MonsterTypeEntity() {
    }

    public String getName() {
        return name;
    }
}
