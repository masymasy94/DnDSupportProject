package com.dndplatform.asset.domain.model;

import java.io.InputStream;

public record DocumentContent(
        String fileName,
        String contentType,
        long size,
        InputStream inputStream
) {
}
