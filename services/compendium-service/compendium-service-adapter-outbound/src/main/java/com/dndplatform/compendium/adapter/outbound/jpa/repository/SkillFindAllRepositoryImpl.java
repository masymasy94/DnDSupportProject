package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SkillMapper;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SkillFindAllRepositoryImpl implements SkillFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SkillPanacheRepository panacheRepository;
    private final SkillMapper mapper;

    @Inject
    public SkillFindAllRepositoryImpl(SkillPanacheRepository panacheRepository, SkillMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Skill> findAllSkills() {
        log.info(() -> "Finding all skills");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}
