package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterSummaryMapper;
import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.domain.model.PagedResultBuilder;
import com.dndplatform.character.domain.repository.CharacterFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterFindAllRepositoryJpa implements CharacterFindAllRepository, PanacheRepository<CharacterEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterSummaryMapper mapper;

    @Inject
    public CharacterFindAllRepositoryJpa(CharacterSummaryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PagedResult findAll(int page, int size) {
        log.info(() -> "Finding all characters - page: %d, size: %d".formatted(page, size));

        var panacheQuery = findAll();

        long totalElements = panacheQuery.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<CharacterSummary> content = panacheQuery
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(mapper)
                .toList();

        return getPagedResult(page, size, content, totalElements, totalPages);
    }

    private static @NonNull PagedResult getPagedResult(int page, int size, List<CharacterSummary> content, long totalElements, int totalPages) {
        return PagedResultBuilder.builder()
                .withContent(content)
                .withPage(page)
                .withSize(size)
                .withTotalElements(totalElements)
                .withTotalPages(totalPages)
                .build();
    }
}
