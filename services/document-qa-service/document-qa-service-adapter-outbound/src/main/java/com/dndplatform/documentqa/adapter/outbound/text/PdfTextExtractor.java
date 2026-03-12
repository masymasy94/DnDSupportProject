package com.dndplatform.documentqa.adapter.outbound.text;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class PdfTextExtractor {

    private final Logger log = Logger.getLogger(getClass().getName());

    public String extract(InputStream inputStream) {
        PDDocument document = null;
        try {
            document = Loader.loadPDF(inputStream.readAllBytes());
            String text = new PDFTextStripper().getText(document);
            log.info(() -> "Extracted %d characters from PDF".formatted(text.length()));
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from PDF", e);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    log.warning(() -> "Failed to close PDF document: " + e.getMessage());
                }
            }
        }
    }
}
