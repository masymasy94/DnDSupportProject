package com.dndplatform.asset.adapter.inbound.documents.list;

import com.dndplatform.asset.domain.DocumentListService;
import com.dndplatform.asset.view.model.DocumentListResource;
import com.dndplatform.asset.view.model.vm.DocumentListItemViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class DocumentListDelegate implements DocumentListResource {

    private final DocumentListService service;

    @Inject
    public DocumentListDelegate(DocumentListService service) {
        this.service = service;
    }

    @Override
    public List<DocumentListItemViewModel> listAll() {
        return service.listAll().stream()
                .map(item -> new DocumentListItemViewModel(item.id(), item.fileName()))
                .toList();
    }
}
