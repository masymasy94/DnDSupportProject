package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;

public interface SpeciesFindByIdResource {
    SpeciesViewModel findById(int id);
}
