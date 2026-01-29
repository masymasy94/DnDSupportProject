package com.dndplatform.asset.domain.repository;

import com.dndplatform.asset.domain.model.DocumentContent;

public interface DocumentDownloadRepository {

    DocumentContent download(String documentId);
}
