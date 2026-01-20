package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "monster_sizes")
public class MonsterSizeEntity extends PanacheEntity {

    @Column(name = "name", length = 15, nullable = false, unique = true)
    public String name;

    @Column(name = "space", length = 20, nullable = false)
    public String space;

    public MonsterSizeEntity() {
    }

    public String getName() {
        return name;
    }

    public String getSpace() {
        return space;
    }
}
