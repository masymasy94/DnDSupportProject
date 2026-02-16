package com.dndplatform.character.view.model;

import com.dndplatform.character.view.model.vm.CharacterViewModel;

import java.io.InputStream;

public interface CharacterImportSheetResource {

    CharacterViewModel importSheet(InputStream pdfData, String fileName);
}
