package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;

import java.util.List;

public interface ToolTypeFindAllResource {
    List<ToolTypeViewModel> findAll(String category);
}
