package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ToolTypeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ToolTypePanacheRepository implements PanacheRepository<ToolTypeEntity> {

    public List<ToolTypeEntity> findByCategory(String category) {
        return find("category", Sort.by("name"), category).list();
    }
}
