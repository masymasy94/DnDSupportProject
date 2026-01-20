package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "proficiency_types")
public class ProficiencyTypeEntity extends PanacheEntity {

    @Column(name = "name", length = 20, nullable = false, unique = true)
    public String name;

    public String getName() {
        return name;
    }
}
