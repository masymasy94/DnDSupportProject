package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "proficiency_types")
public class ProficiencyTypeEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Short id;

    @Column(name = "name", length = 20, nullable = false, unique = true)
    public String name;

    public Short getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
