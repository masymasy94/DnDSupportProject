package com.dndplatform.documentqa.domain.impl;

import java.io.InputStream;

public interface TextExtractorRouter {

    String extract(InputStream inputStream, String contentType, String fileName);
}
