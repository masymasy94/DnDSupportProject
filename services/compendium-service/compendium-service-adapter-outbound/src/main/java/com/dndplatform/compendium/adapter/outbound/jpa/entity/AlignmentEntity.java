package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "alignments")
public class AlignmentEntity extends PanacheEntity {

    @Column(name = "code", length = 2, nullable = false, unique = true)
    public String code;

    @Column(name = "name", length = 20, nullable = false, unique = true)
    public String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
