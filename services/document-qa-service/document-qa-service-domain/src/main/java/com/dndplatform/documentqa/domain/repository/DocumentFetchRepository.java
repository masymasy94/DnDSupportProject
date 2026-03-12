package com.dndplatform.documentqa.domain.repository;

import java.io.InputStream;

public interface DocumentFetchRepository {

    InputStream fetch(String documentId, String fileName);
}
