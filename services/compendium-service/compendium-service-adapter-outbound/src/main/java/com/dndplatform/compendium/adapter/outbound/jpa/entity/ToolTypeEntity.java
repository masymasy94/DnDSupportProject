package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tool_types")
public class ToolTypeEntity extends PanacheEntity {

    @Column(name = "name", length = 50, nullable = false)
    public String name;

    @Column(name = "category", length = 20, nullable = false)
    public String category;

    public ToolTypeEntity() {}

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}
