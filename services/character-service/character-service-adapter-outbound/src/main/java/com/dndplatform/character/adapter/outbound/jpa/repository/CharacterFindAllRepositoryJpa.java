package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterSummaryMapper;
import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.domain.model.PagedResultBuilder;
import com.dndplatform.character.domain.repository.CharacterFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterFindAllRepositoryJpa implements CharacterFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterSummaryMapper mapper;
    private final CharacterPanacheRepository panacheRepository;

    @Inject
    public CharacterFindAllRepositoryJpa(CharacterSummaryMapper mapper, CharacterPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public PagedResult findAll(int page, int size) {
        log.info(() -> "Finding all characters - page: %d, size: %d".formatted(page, size));

        long totalElements = panacheRepository.countAll();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<CharacterSummary> content = panacheRepository.findAllPaged(page, size)
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
