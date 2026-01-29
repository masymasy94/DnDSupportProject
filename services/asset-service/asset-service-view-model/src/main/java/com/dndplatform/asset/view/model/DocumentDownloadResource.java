package com.dndplatform.asset.view.model;

import jakarta.ws.rs.core.Response;

public interface DocumentDownloadResource {

    Response download(String documentId);
}
