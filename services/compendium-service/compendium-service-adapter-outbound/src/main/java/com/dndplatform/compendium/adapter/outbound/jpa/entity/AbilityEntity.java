package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "abilities")
public class AbilityEntity extends PanacheEntity {

    @Column(name = "code", length = 3, nullable = false, unique = true)
    public String code;

    @Column(name = "name", length = 15, nullable = false, unique = true)
    public String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
