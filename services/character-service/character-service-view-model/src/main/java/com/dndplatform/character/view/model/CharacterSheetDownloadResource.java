package com.dndplatform.character.view.model;

import jakarta.ws.rs.core.Response;

public interface CharacterSheetDownloadResource {
    Response downloadSheet(Long characterId);
}
