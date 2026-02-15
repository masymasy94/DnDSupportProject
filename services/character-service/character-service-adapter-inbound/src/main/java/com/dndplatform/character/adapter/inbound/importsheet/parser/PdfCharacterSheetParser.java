package com.dndplatform.character.adapter.inbound.importsheet.parser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class PdfCharacterSheetParser {

    private final Logger log = Logger.getLogger(getClass().getName());

    public Map<String, String> extractFormFields(byte[] pdfBytes) {
        Map<String, String> fields = new LinkedHashMap<>();

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new BadRequestException("PDF does not contain a fillable form (no AcroForm found)");
            }

            for (PDField field : acroForm.getFieldTree()) {
                String name = field.getFullyQualifiedName().trim();
                String value = field.getValueAsString();
                fields.put(name, value != null ? value.trim() : "");
                log.fine(() -> "PDF field: '%s' = '%s'".formatted(name, value));
            }

            log.info(() -> "Extracted %d form fields from PDF".formatted(fields.size()));
        } catch (BadRequestException e) {
            throw e;
        } catch (IOException e) {
            throw new BadRequestException("Failed to parse PDF: " + e.getMessage());
        }

        if (fields.isEmpty()) {
            throw new BadRequestException("PDF form contains no fields");
        }

        return fields;
    }
}
