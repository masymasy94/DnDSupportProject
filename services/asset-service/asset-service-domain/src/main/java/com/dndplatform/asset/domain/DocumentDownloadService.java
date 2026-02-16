package com.dndplatform.asset.domain;

import com.dndplatform.asset.domain.model.DocumentContent;

public interface DocumentDownloadService {

    DocumentContent download(String documentId);
}
