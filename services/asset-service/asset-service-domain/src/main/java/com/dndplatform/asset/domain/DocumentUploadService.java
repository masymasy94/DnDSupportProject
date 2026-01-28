package com.dndplatform.asset.domain;

import com.dndplatform.asset.domain.model.Document;

import java.io.InputStream;

public interface DocumentUploadService {

    Document upload(String fileName, String contentType, InputStream inputStream, long size, String uploadedBy);
}
