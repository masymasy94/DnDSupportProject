package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.CharacterSheetGenerator;
import com.dndplatform.character.domain.model.Character;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class PdfCharacterSheetGenerator implements CharacterSheetGenerator {

    private static final Logger log = Logger.getLogger(PdfCharacterSheetGenerator.class.getName());

    private static final String TEMPLATE_PATH = "META-INF/resources/wotc-5e-sheet.pdf";

    private final CharacterFieldMapper fieldMapper;

    @Inject
    public PdfCharacterSheetGenerator(CharacterFieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    @Override
    public byte[] generate(Character character) {
        byte[] templateBytes = loadTemplate();
        CharacterFieldMap fieldMap = fieldMapper.mapFields(character);

        try (PDDocument document = Loader.loadPDF(templateBytes)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IOException("PDF template does not contain an AcroForm");
            }

            Map<String, PDField> pdfFieldMap = buildFieldMap(acroForm);
            acroForm.setNeedAppearances(false);

            for (Map.Entry<String, String> entry : fieldMap.textFields().entrySet()) {
                setTextField(pdfFieldMap, entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Boolean> entry : fieldMap.checkboxFields().entrySet()) {
                setCheckBox(pdfFieldMap, entry.getKey(), entry.getValue());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate character sheet PDF", e);
        }
    }

    byte[] loadTemplate() {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                throw new RuntimeException("PDF template not found on classpath: " + TEMPLATE_PATH);
            }
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF template", e);
        }
    }

    private Map<String, PDField> buildFieldMap(PDAcroForm acroForm) {
        Map<String, PDField> map = new HashMap<>();
        for (PDField field : acroForm.getFieldTree()) {
            String name = field.getFullyQualifiedName();
            map.put(name, field);
            String trimmed = name.trim();
            if (!trimmed.equals(name)) {
                map.putIfAbsent(trimmed, field);
            }
        }
        return map;
    }

    private void setTextField(Map<String, PDField> fieldMap, String name, String value) {
        if (value == null || value.isBlank()) return;

        PDField field = fieldMap.get(name);
        if (field == null) {
            log.fine(() -> "PDF field not found: '%s'".formatted(name));
            return;
        }

        try {
            field.setValue(value);
        } catch (IOException e) {
            log.warning(() -> "Failed to set PDF field '%s': %s".formatted(name, e.getMessage()));
        }
    }

    private void setCheckBox(Map<String, PDField> fieldMap, String name, boolean checked) {
        PDField field = fieldMap.get(name);
        if (field == null) {
            log.fine(() -> "PDF checkbox not found: '%s'".formatted(name));
            return;
        }

        if (field instanceof PDCheckBox checkbox) {
            try {
                if (checked) {
                    checkbox.check();
                } else {
                    checkbox.unCheck();
                }
            } catch (IOException e) {
                log.warning(() -> "Failed to set checkbox '%s': %s".formatted(name, e.getMessage()));
            }
        }
    }
}
