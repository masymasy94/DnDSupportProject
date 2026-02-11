package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.PagedSpellViewModel;

import java.util.List;

public interface SpellFindAllResource {
    PagedSpellViewModel findAll(String search, List<Integer> levels, List<String> schools, Boolean concentration, Boolean ritual,
                                Integer page, Integer pageSize);
}
