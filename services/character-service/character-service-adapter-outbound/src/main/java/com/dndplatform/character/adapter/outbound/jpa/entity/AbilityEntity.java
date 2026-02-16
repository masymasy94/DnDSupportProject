package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "abilities")
public class AbilityEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Short id;

    @Column(name = "code", length = 3, nullable = false, unique = true)
    public String code;

    @Column(name = "name", length = 15, nullable = false, unique = true)
    public String name;

    public Short getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
