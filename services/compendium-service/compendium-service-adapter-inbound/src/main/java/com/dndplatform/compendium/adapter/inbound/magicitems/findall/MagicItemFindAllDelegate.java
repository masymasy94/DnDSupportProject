package com.dndplatform.compendium.adapter.inbound.magicitems.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.magicitems.findall.mapper.MagicItemViewModelMapper;
import com.dndplatform.compendium.domain.MagicItemFindAllService;
import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.view.model.MagicItemFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedMagicItemViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

@Delegate
@RequestScoped
public class MagicItemFindAllDelegate implements MagicItemFindAllResource {

    private final MagicItemFindAllService service;
    private final MagicItemViewModelMapper mapper;

    @Inject
    public MagicItemFindAllDelegate(MagicItemFindAllService service,
                                    MagicItemViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedMagicItemViewModel findAll(String name, String rarity, String type,
                                           Boolean requiresAttunement,
                                           Integer page, Integer pageSize) {
        var criteria = getMagicItemFilterCriteria(name, rarity, type, requiresAttunement, page, pageSize);
        var result = service.findAll(criteria);
        return getPagedResult(result);
    }

    private @NonNull PagedMagicItemViewModel getPagedResult(PagedResult<MagicItem> result) {
        return new PagedMagicItemViewModel(
                result.content().stream().map(mapper).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }

    private static @NonNull MagicItemFilterCriteria getMagicItemFilterCriteria(
            String name, String rarity, String type, Boolean requiresAttunement,
            Integer page, Integer pageSize) {
        return new MagicItemFilterCriteria(
                name,
                rarity,
                type,
                requiresAttunement,
                page != null ? page : MagicItemFilterCriteria.DEFAULT_PAGE,
                pageSize != null ? pageSize : MagicItemFilterCriteria.DEFAULT_PAGE_SIZE
        );
    }
}
