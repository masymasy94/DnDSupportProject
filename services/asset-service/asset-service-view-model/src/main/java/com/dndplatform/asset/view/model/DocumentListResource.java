package com.dndplatform.asset.view.model;

import com.dndplatform.asset.view.model.vm.DocumentListItemViewModel;

import java.util.List;

public interface DocumentListResource {

    List<DocumentListItemViewModel> listAll();
}
