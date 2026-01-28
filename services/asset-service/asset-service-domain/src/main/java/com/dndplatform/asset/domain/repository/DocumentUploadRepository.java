package com.dndplatform.asset.domain.repository;

import com.dndplatform.asset.domain.model.Document;

import java.io.InputStream;

public interface DocumentUploadRepository {

    Document upload(String fileName, String contentType, InputStream inputStream, long size, String uploadedBy);
}