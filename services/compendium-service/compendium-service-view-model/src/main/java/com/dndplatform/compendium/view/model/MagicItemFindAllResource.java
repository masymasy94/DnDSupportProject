package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.PagedMagicItemViewModel;

public interface MagicItemFindAllResource {
    PagedMagicItemViewModel findAll(
            String name,
            String rarity,
            String type,
            Boolean requiresAttunement,
            Integer page,
            Integer pageSize
    );
}
