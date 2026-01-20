package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "damage_types")
public class DamageTypeEntity extends PanacheEntity {

    @Column(name = "name", length = 30, nullable = false, unique = true)
    public String name;

    public DamageTypeEntity() {}

    public String getName() {
        return name;
    }
}
