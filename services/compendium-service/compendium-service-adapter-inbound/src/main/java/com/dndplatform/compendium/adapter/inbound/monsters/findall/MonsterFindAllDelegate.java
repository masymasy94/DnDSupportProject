package com.dndplatform.compendium.adapter.inbound.monsters.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.monsters.findall.mapper.MonsterViewModelMapper;
import com.dndplatform.compendium.domain.MonsterFindAllService;
import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.view.model.MonsterFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedMonsterViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

@Delegate
@RequestScoped
public class MonsterFindAllDelegate implements MonsterFindAllResource {

    private final MonsterFindAllService service;
    private final MonsterViewModelMapper mapper;

    @Inject
    public MonsterFindAllDelegate(MonsterFindAllService service,
                                  MonsterViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedMonsterViewModel findAll(String name, String type, String size,
                                         String challengeRating, String alignment,
                                         Integer page, Integer pageSize) {


        var criteria = getMonsterFilterCriteria(name, type, size, challengeRating, alignment, page, pageSize);
        var result = service.findAll(criteria);
        return getPagedResult(result);
    }


    private @NonNull PagedMonsterViewModel getPagedResult(PagedResult<Monster> result) {
        return new PagedMonsterViewModel(
                result.content().stream().map(mapper).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }


    private static @NonNull MonsterFilterCriteria getMonsterFilterCriteria(String name, String type, String size, String challengeRating, String alignment, Integer page, Integer pageSize) {
        return new MonsterFilterCriteria(
                name,
                type,
                size,
                challengeRating,
                alignment,
                page != null ? page : MonsterFilterCriteria.DEFAULT_PAGE,
                pageSize != null ? pageSize : MonsterFilterCriteria.DEFAULT_PAGE_SIZE
        );
    }
}
