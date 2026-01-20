package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.PagedMonsterViewModel;

public interface MonsterFindAllResource {
    PagedMonsterViewModel findAll(
            String name,
            String type,
            String size,
            String challengeRating,
            String alignment,
            Integer page,
            Integer pageSize
    );
}
