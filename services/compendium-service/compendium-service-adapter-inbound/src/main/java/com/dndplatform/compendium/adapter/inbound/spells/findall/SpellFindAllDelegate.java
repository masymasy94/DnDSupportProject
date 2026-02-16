package com.dndplatform.compendium.adapter.inbound.spells.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.spells.findall.mapper.SpellViewModelMapper;
import com.dndplatform.compendium.domain.SpellFindAllService;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.view.model.SpellFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedSpellViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.util.List;

@Delegate
@RequestScoped
public class SpellFindAllDelegate implements SpellFindAllResource {

    private final SpellFindAllService service;
    private final SpellViewModelMapper mapper;

    @Inject
    public SpellFindAllDelegate(SpellFindAllService service,
                                SpellViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedSpellViewModel findAll(String search, List<Integer> levels, List<String> schools, Boolean concentration, Boolean ritual,
                                       Integer page, Integer pageSize) {
        var criteria = getSpellFilterCriteria(search, levels, schools, concentration, ritual, page, pageSize);
        var result = service.findAll(criteria);
        return getPagedResult(result);
    }

    private @NonNull PagedSpellViewModel getPagedResult(PagedResult<Spell> result) {
        return new PagedSpellViewModel(
                result.content().stream().map(mapper).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }

    private static @NonNull SpellFilterCriteria getSpellFilterCriteria(String search, List<Integer> levels, List<String> schools,
                                                                       Boolean concentration, Boolean ritual,
                                                                       Integer page, Integer pageSize) {
        return new SpellFilterCriteria(
                search != null && !search.isBlank() ? search.trim() : null,
                levels != null && !levels.isEmpty() ? levels : null,
                schools != null && !schools.isEmpty() ? schools : null,
                concentration,
                ritual,
                page != null ? page : SpellFilterCriteria.DEFAULT_PAGE,
                pageSize != null ? pageSize : SpellFilterCriteria.DEFAULT_PAGE_SIZE
        );
    }
}
