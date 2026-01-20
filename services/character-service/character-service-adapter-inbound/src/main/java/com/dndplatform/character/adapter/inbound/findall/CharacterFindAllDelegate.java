package com.dndplatform.character.adapter.inbound.findall;

import com.dndplatform.character.adapter.inbound.findall.mapper.CharacterSummaryViewModelMapper;
import com.dndplatform.character.domain.CharacterFindAllService;
import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.view.model.CharacterFindAllResource;
import com.dndplatform.character.view.model.vm.CharacterSummaryViewModel;
import com.dndplatform.character.view.model.vm.PagedCharactersViewModel;
import com.dndplatform.character.view.model.vm.PagedCharactersViewModelBuilder;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CharacterFindAllDelegate implements CharacterFindAllResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterFindAllService service;
    private final CharacterSummaryViewModelMapper mapper;

    @Inject
    public CharacterFindAllDelegate(CharacterFindAllService service,
                                    CharacterSummaryViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedCharactersViewModel findAll(int page, int size) {
        log.info(() -> "Finding all characters - page: %d, size: %d".formatted(page, size));

        var result = service.findAll(page, size);
        return getResponse(result);
    }

    @Nonnull
    private PagedCharactersViewModel getResponse(PagedResult result) {

        var content = result.content().stream()
                .map(mapper)
                .toList();

        return PagedCharactersViewModelBuilder.builder()
                .withContent(content)
                .withPage(result.page())
                .withSize(result.size())
                .withTotalElements(result.totalElements())
                .withTotalPages(result.totalPages())
                .build();
    }
}
