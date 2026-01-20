package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.WeaponTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.WeaponTypeMapper;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class WeaponTypeFindAllRepositoryImpl implements WeaponTypeFindAllRepository, PanacheRepository<WeaponTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final WeaponTypeMapper mapper;

    @Inject
    public WeaponTypeFindAllRepositoryImpl(WeaponTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<WeaponType> findAllWeaponTypes(String category) {
        log.info(() -> "Finding all weapon types" + (category != null ? " with category: " + category : ""));

        if (category != null && !category.isBlank()) {
            return find("category", Sort.by("name"), category).list().stream()
                    .map(mapper)
                    .toList();
        }

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}
