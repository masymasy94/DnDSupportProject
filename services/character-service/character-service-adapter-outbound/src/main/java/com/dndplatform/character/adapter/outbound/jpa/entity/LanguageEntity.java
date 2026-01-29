package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "languages")
public class LanguageEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Short id;

    @Column(name = "name", length = 20, nullable = false, unique = true)
    public String name;

    @Column(name = "script", length = 20)
    public String script;

    @Column(name = "type", length = 10, nullable = false)
    public String type;

    public Short getId() {
        return id;
    }

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
