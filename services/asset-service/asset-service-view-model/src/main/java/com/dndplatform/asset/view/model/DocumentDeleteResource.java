package com.dndplatform.asset.view.model;

import jakarta.ws.rs.core.Response;

public interface DocumentDeleteResource {

    Response delete(String documentId);
}
