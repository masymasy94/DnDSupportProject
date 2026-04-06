package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.WeaponTypeMapper;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class WeaponTypeFindAllRepositoryImpl implements WeaponTypeFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final WeaponTypePanacheRepository panacheRepository;
    private final WeaponTypeMapper mapper;

    @Inject
    public WeaponTypeFindAllRepositoryImpl(WeaponTypePanacheRepository panacheRepository, WeaponTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<WeaponType> findAllWeaponTypes(String category) {
        log.info(() -> "Finding all weapon types" + (category != null ? " with category: " + category : ""));

        if (category != null && !category.isBlank()) {
            return panacheRepository.findByCategory(category).stream()
                    .map(mapper)
                    .toList();
        }

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}
