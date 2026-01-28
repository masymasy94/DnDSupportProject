package com.dndplatform.asset.adapter.inbound.documents.download;

import com.dndplatform.asset.domain.DocumentDownloadService;
import com.dndplatform.asset.view.model.DocumentDownloadResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@Delegate
@RequestScoped
public class DocumentDownloadDelegate implements DocumentDownloadResource {

    private final DocumentDownloadService service;

    @Inject
    public DocumentDownloadDelegate(DocumentDownloadService service) {
        this.service = service;
    }

    @Override
    public Response download(String documentId) {
        var content = service.download(documentId);
        return Response.ok(content.inputStream())
                .header("Content-Type", content.contentType())
                .header("Content-Disposition", "attachment; filename=\"" + content.fileName() + "\"")
                .header("Content-Length", content.size())
                .build();
    }
}
