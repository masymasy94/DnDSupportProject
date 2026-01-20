package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "spell_schools")
public class SpellSchoolEntity extends PanacheEntity {

    @Column(name = "name", length = 50, nullable = false)
    public String name;

    public SpellSchoolEntity() {
    }

    public String getName() {
        return name;
    }
}
