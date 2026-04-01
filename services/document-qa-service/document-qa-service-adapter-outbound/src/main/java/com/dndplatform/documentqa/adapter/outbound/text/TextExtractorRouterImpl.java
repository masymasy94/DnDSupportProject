package com.dndplatform.documentqa.adapter.outbound.text;

import com.dndplatform.documentqa.domain.impl.TextExtractorRouter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class TextExtractorRouterImpl implements TextExtractorRouter {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Inject
    PdfTextExtractor pdfTextExtractor;

    @Inject
    PlainTextExtractor plainTextExtractor;

    @Override
    public String extract(InputStream inputStream, String contentType, String fileName) {
        log.info(() -> "Routing text extraction for contentType=%s, fileName=%s".formatted(contentType, fileName));

        if ("application/pdf".equals(contentType)) {
            return pdfTextExtractor.extract(inputStream);
        }
        return plainTextExtractor.extract(inputStream);
    }
}
