package com.dndplatform.asset.adapter.inbound.documents.delete;

import com.dndplatform.asset.domain.DocumentDeleteService;
import com.dndplatform.asset.view.model.DocumentDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@Delegate
@RequestScoped
public class DocumentDeleteDelegate implements DocumentDeleteResource {

    private final DocumentDeleteService service;

    @Inject
    public DocumentDeleteDelegate(DocumentDeleteService service) {
        this.service = service;
    }

    @Override
    public Response delete(String documentId) {
        service.delete(documentId);
        return Response.noContent().build();
    }
}
