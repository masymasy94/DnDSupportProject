package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.WeaponTypeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WeaponTypePanacheRepository implements PanacheRepository<WeaponTypeEntity> {

    public List<WeaponTypeEntity> findByCategory(String category) {
        return find("category", Sort.by("name"), category).list();
    }
}
