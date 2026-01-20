package com.dndplatform.compendium.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "languages")
public class LanguageEntity extends PanacheEntity {

    @Column(name = "name", length = 30, nullable = false, unique = true)
    public String name;

    @Column(name = "script", length = 20)
    public String script;

    @Column(name = "type", length = 10, nullable = false)
    public String type;

    public String getName() {
        return name;
    }

    public String getScript() {
        return script;
    }

    public String getType() {
        return type;
    }
}
